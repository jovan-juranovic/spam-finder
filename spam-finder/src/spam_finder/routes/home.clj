(ns spam-finder.routes.home
  (:require [compojure.core :refer :all]
            [noir.session :as session]
            [noir.response :as resp]
            [noir.util.route :refer [restricted]]
            [spam-finder.views.layout :as layout]
            [spam-finder.models.db :as db]))

(defn home-page []
  (layout/render
    "home.html"))

(defroutes home-routes
  (GET "/Home" []
       (restricted(home-page))))