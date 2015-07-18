(ns spam-finder.routes.editor
  (:require [compojure.core :refer :all]
            [noir.session :as session]
            [noir.response :as resp]
            [noir.util.route :refer [restricted]]
            [spam-finder.views.layout :as layout]
            [spam-finder.models.learning-model :refer [classify train!]]
            [spam-finder.models.word-counter :refer [get-sorted-words-by-freq]]))

(defn text-editor-page []
  (layout/render "text-editor.html"))

(defn handle-submit [text]
  (let [[k v] (classify text)]
    (resp/json {:resp (str (name k) " with probability: " (format "%.2f" (* 100 v)) "%")})))

(defn handle-train [val type]
  (if (= type "spam")
    (train! val :spam)
    (train! val :ham))
  (resp/json {:Ok 1}))

(defn handle-counter [text]
  (resp/json {:data (get-sorted-words-by-freq text)}))
(defroutes editor-routes
   (GET "/editor" [] (restricted (text-editor-page)))
(POST "/analyze" [text] (restricted (handle-submit text)))
(POST "/train" [val type] (restricted (handle-train val type)))
(POST "/counter" [text] (restricted (handle-counter text))))
