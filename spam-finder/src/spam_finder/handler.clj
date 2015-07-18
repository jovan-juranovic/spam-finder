(ns spam-finder.handler
  (:require [compojure.core :refer [defroutes]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.file-info :refer [wrap-file-info]]
            [hiccup.middleware :refer [wrap-base-url]]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [noir.util.middleware :as noir-middleware]
            [noir.session :as session]
            [spam-finder.routes.editor :refer [editor-routes]]
            [spam-finder.routes.auth :refer [auth-routes]]
            [spam-finder.routes.admin :refer [admin-routes]]
            [spam-finder.routes.home :refer [home-routes]]
            [spam-finder.routes.user :refer [user-profile-routes]]
            [spam-finder.models.learning-model :refer [train-and-cv-classifier]]
            [spam-finder.models.db :refer [init-db]]
            [spam-finder.views.layout :refer [*servlet-context*]]))

(defn wrap-servlet-context [handler]
  (fn [request]
    (binding [*servlet-context*
              (if-let [context (:servlet-context request)]
                (try (.getContextPath context)
                     (catch IllegalArgumentException _ context)))]
      (handler request))))

(defn init []
  (println "spam-finder is starting\nInitializing database...")
  (init-db)
  (println "Initializing training on datasets...")
  (train-and-cv-classifier 1/5))

(defn destroy []
  (println "spam-finder is shutting down"))

(defroutes app-routes
  (route/resources "/")
  (route/not-found "Not Found"))

(defn user-page [_]
  (session/get :user))

(def app
  (-> [auth-routes home-routes editor-routes user-profile-routes admin-routes app-routes]
      (noir-middleware/app-handler)
      (noir-middleware/wrap-access-rules [user-page])
      (wrap-servlet-context)))