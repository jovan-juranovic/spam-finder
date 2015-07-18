(ns spam-finder.models.word-counter
  (:require [clojure.string :refer [lower-case]]))

(def ^:private ^:const stop-words-filepath "corpus/english.stop")

(defn- tokenize [string]
  (map lower-case (re-seq #"[\p{L}\p{M}]+" string)))

(defn- load-stop-words [filename]
  (set (tokenize (slurp filename))))

(defn- remove-stop-words [tokens]
  (let [stop-words (load-stop-words stop-words-filepath)]
    (remove stop-words tokens)))

(defn- words-frequency [text]
  (frequencies (remove-stop-words (tokenize text))))

(defn get-sorted-words-by-freq [text]
  (reverse (sort-by second (words-frequency text))))