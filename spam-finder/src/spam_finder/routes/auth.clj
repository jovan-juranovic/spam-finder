(ns spam-finder.routes.auth
  (:require [compojure.core :refer :all]
            [noir.session :as session]
            [noir.response :as resp]
            [noir.util.route :refer [restricted]]
            [noir.validation :refer [rule has-value? min-length? is-email? errors? on-error get-errors]]
            [clojure.java.io :as io]
            [spam-finder.views.layout :as layout]
            [spam-finder.models.db :as db]))

(defn valid? [uid pass pass1 first-name last-name email]
  (rule (has-value? uid)
        [:uid "Username is required !"])
  (rule (db/username-not-exists? uid)
        [:uid "Username is already taken !"])
  (rule (has-value? first-name)
        [:first-name "First name is required !"])
  (rule (has-value? last-name)
        [:last-name "Last name is required !"])
  (rule (has-value? email)
        [:email "E-mail is required !"])
  (rule (is-email? email)
        [:email "E-mail is not in the valid format !"])
  (rule (min-length? pass 5)
        [:pass "Password must be at least 5 characters !"])
  (rule (= pass pass1)
        [:pass "Entered passwords do not match !"])
  (not (errors? :uid :pass :pass1 :first-name :last-name :email)))

(defn registration-page [& [uid first-name last-name email]]
  (layout/render "signup.html" 
                 {:uid uid
                  :first-name first-name
                  :last-name last-name
                  :email email
                  :uid-error (first (get-errors :uid))
                  :first-name-error (first (get-errors :first-name))
                  :last-name-error (first (get-errors :last-name))
                  :email-error (first (get-errors :email))
                  :pass-error (first (get-errors :pass))}))

(defn handle-registration [uid pass pass1 first-name last-name email]
  (if (valid? uid pass pass1 first-name last-name email)
    (do
      (db/create-new-user uid pass first-name last-name email "false")
      (session/put! :user uid)
      (resp/redirect "/editor"))
    (registration-page uid first-name last-name email)))

(defn login-page []
  (if (session/get :user)
    (session/clear!))
  (layout/render "login.html"))

(defn redirect-to [uid]
  (if (db/user-is-admin? uid)
    (resp/redirect "/administration")
    (resp/redirect "/Home")))

(defn handle-login [uid pass]
  (if (db/valid-user? uid pass)
    (do
      (session/put! :user uid)
      (redirect-to uid))
    (resp/redirect "/")))

(defn handle-logout []
  (session/clear!)
  (resp/redirect "/"))

(defroutes auth-routes
  (GET "/" [] 
       (login-page))
  (POST "/login" [uid pass]
        (handle-login uid pass))
  (GET "/register" []
       (registration-page))
  (POST "/register" [uid pass pass1 first-name last-name email]
        (handle-registration uid pass pass1 first-name last-name email))
  (GET "/logout" []
       (restricted (handle-logout))))