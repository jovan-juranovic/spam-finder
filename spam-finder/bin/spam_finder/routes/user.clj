(ns spam-finder.routes.user
  (:require [compojure.core :refer :all]
            [noir.session :as session]
            [noir.response :as resp]
            [noir.util.route :refer [restricted]]
            [spam-finder.views.layout :as layout]
            [spam-finder.models.db :as db]))

(defn profile-page []
  (layout/render "profile.html"))

(defn get-user []
  (resp/json (db/get-user-by-username (session/get :user))))

(defn update-user [uid first-name last-name email]
  (if (db/update-user-by-username uid first-name last-name email)
    (resp/json {:Ok 1})
    (resp/json {:Ok 0})))

(defroutes user-profile-routes
  (GET "/profile" []
       (restricted (profile-page)))
  (GET "/user_profile" []
       (restricted (get-user)))
  (POST "/edit_profile" [uid first_name last_name email]
        (restricted (update-user uid first_name last_name email))))