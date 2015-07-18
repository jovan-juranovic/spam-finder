(ns spam-finder.models.learning-model
  (:require [clojure.java.io :refer [file]]
            [incanter.stats :refer [cdf-chisq]]))

(def ^:private ^:const min-spam-score 0.7)
(def ^:private ^:const max-ham-score 0.4)

(def ^:private ^:const token-regex #"[a-zA-Z]{3,}")
(def ^:private ^:const header-fields
  ["To:"
   "From:"
   "Subject:"
   "Return-Path:"])

(def ^:private feature-db
  (agent {} :error-handler #(println "Error: " %2)))

(def ^:private total-ham (agent 0))
(def ^:private total-spam (agent 0))

(defrecord TokenFeature [token spam ham])

(defn- new-token [token]
  (TokenFeature. token 0 0))

(defn- classify-score [score]
  [(cond
     (<= score max-ham-score) :ham
     (>= score min-spam-score) :spam
     :else :unsure)
   score])

(defn- inc-count [token-feature type]
  (update-in token-feature [type] inc))

(defn- clear-db []
  (send feature-db (constantly {}))
  (send total-ham  (constantly 0))
  (send total-spam (constantly 0)))

(defn- update-feature!
  "Looks up a TokenFeature record in the database and
  creates it if it doesn't exist, or updates it."
  [token f & args]
  (send feature-db update-in [token]
        #(apply f (if %1 %1 (new-token token))
                args)))

(defn- header-token-regex [f]
  (re-pattern (str f "(.*)\n")))

(defn- extract-tokens-from-headers [text]
  (for [field header-fields]
    (map #(str field %1)
         (mapcat (fn [x] (->> x second (re-seq token-regex)))
                 (re-seq (header-token-regex field)
                         text)))))

(defn- extract-tokens [text]
  (apply concat
         (re-seq token-regex text)
         (extract-tokens-from-headers text)))

(defn- update-features!
  "Updates or creates a TokenFeature in database
  for each token in text."
  [text f & args]
  (doseq [token (extract-tokens text)]
    (apply update-feature! token f args)))

(defn- extract-features [text]
  "Extracts all known tokens from text"
  (keep identity (map #(@feature-db %1) (extract-tokens text))))

(defn- inc-total-count! [type]
  (send (case type
          :spam total-spam
          :ham total-ham)
        inc))

(defn train! [text type]
  (update-features! text inc-count type)
  (inc-total-count! type))

(defn- spam-probability [feature]
  (let [s (/ (:spam feature) (max 1 @total-spam))
        h (/ (:ham feature) (max 1 @total-ham))]
    (/ s (+ s h))))

(defn- bayesian-spam-probability
  "Calculates probability a feature is spam on a prior
  probability assumed-probability for each feature,
  and weight is the weight to be given to the prior
  assumed (i.e. the number of data points)."
  [feature & {:keys [assumed-probability weight]
              :or   {assumed-probability 1/2 weight 1}}]
  (let [basic-prob (spam-probability feature)
        total-count (+ (:spam feature) (:ham feature))]
    (/ (+ (* weight assumed-probability)
          (* total-count basic-prob))
       (+ weight total-count))))

(defn- fisher
  "Combines several probabilities with Fisher's method."
  [probs]
  (- 1 (cdf-chisq
         (* -2 (reduce + (map #(Math/log %1) probs)))
         :df (* 2 (count probs)))))

(defn- score [features]
  (let [spam-probs (map bayesian-spam-probability features)
        ham-probs (map #(- 1 %1) spam-probs)
        h (- 1 (fisher spam-probs))
        s (- 1 (fisher ham-probs))]
    (/ (+ (- 1 h) s) 2)))

(defn classify
  "Returns a vector of the form [classification score]"
  [text]
  (-> text
    extract-features
    score
    classify-score))

(defn- train-from-corpus! [corpus]
  (doseq [v corpus]
    (let [[filename type] v]
      (train! (slurp filename) type))))

(defn- cv-from-corpus [corpus]
  (for [v corpus]
    (let [[filename type] v
          [classification score] (classify (slurp filename))]
      {:filename filename
       :type type
       :classification classification
       :score score})))

(defn- test-classifier! [corpus cv-fraction]
  "Trains and cross-validates the classifier with the sample
  data in corpus, using cv-fraction for cross-validation.
  Returns a sequence of maps representing the results
  of the cross-validation."
  (clear-db)
  (let [shuffled (shuffle corpus)
        size (count corpus)
        training-num (* size (- 1 cv-fraction))
        training-set (take training-num shuffled)
        cv-set (nthrest shuffled training-num)]
    (train-from-corpus! training-set)
    (await feature-db)
    (cv-from-corpus cv-set)))

(defn- populate-emails
  "Returns a sequence of vectors of the form [filename type]"
  []
  (letfn [(get-email-files [type]
            (map (fn [f] [(.toString f) (keyword type)])
                 (rest (file-seq (file (str "corpus/" type))))))]
    (mapcat get-email-files ["ham" "spam"])))

(defn- result-type [{:keys [filename type classification score]}]
  (case type
    :ham  (case classification
            :ham :correct
            :spam :false-positive
            :unsure :missed-ham)
    :spam (case classification
            :spam :correct
            :ham :false-negative
            :unsure :missed-spam)))

(defn- analyze-results [results]
  (reduce (fn [map result]
            (let [type (result-type result)]
              (update-in map [type] inc)))
          {:total (count results) :correct 0 :false-positive 0
           :false-negative 0 :missed-ham 0 :missed-spam 0}
          results))

(defn- print-result [result]
  (let [total (:total result)]
    (doseq [[key num] result]
      (printf "%15s : %-6d%6.2f %%%n"
              (name key) num (float (* 100 (/ num total)))))))

(defn train-and-cv-classifier [cv-frac]
  (if-let [emails (seq (populate-emails))]
    (-> emails
      (test-classifier! cv-frac)
      analyze-results
      print-result)
    (throw (Error. "No mails found !"))))