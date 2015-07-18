(ns spam-finder.routes.admin
  (:require [compojure.core :refer :all]
            [noir.session :as session]
            [noir.response :as resp]
            [noir.util.route :refer [restricted]]
            [spam-finder.views.layout :as layout]
            [spam-finder.models.db :as db]))

(defn admin-page []
  (layout/render "admin.html"))

(defn get-users []
  (resp/json (db/get-all-users)))

(defn create-user [uid first-name last-name email password isAdmin]
  (if (db/create-new-user uid password first-name last-name email isAdmin)
    (resp/json {:Ok 1})
    (resp/json {:Ok 0})))

(defn get-user [uid]
  (resp/json (db/get-user-by-username uid)))

(defn update-user [uid first-name last-name email]
  (if (db/update-user-by-username uid first-name last-name email)
    (resp/json {:Ok 1})
    (resp/json {:Ok 0})))

(defn delete-user [uid]
  (if (db/delete-user-by-username uid)
    (resp/json {:Ok 1})
    (resp/json {:Ok 0})))

(defroutes admin-routes
  (GET "/administration" [] 
       (restricted (admin-page)))
  (GET "/users" []
       (restricted (get-users)))
  (GET "/user" [uid]
       (restricted (get-user uid)))
  (POST "/user" [uid first_name last_name email]
        (restricted (update-user uid first_name last_name email)))
  (POST "/create_user" [uid first_name last_name email password isAdmin]
        (restricted (create-user uid first_name last_name email password isAdmin)))
  (DELETE "/user" [uid]
          (restricted(delete-user uid))))