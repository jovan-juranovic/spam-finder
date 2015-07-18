(ns spam-finder.models.db
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [monger.operators :refer :all])
  (:import (org.bson.types ObjectId)))

(def connection (mg/connect))

(def db (mg/get-db connection "spam_finder_db"))

(defn insert-init-users []
  (mc/insert db "users" {:_id (str (ObjectId.)) :first_name "admin" :last_name "admin" :email "test@mail.com" :username "admin" :password "admin" :isAdmin "true"})
  (mc/insert db "users" {:_id (str (ObjectId.)) :first_name "test" :last_name "user" :email "test@mail.com" :username "user" :password "password" :isAdmin "false"}))

(defn create-new-user [uid pass first-name last-name email isAdmin]
  (mc/insert db "users" {:_id (str (ObjectId.)) :first_name first-name :last_name last-name :email email :username uid :password pass :isAdmin isAdmin}))

(defn get-all-users []
  (mc/find-maps db "users"))

(defn get-user-by-username [username]
      (mc/find-one-as-map db "users" {:username username}))

(defn update-user-by-username [uid first-name last-name email]
  (mc/update db "users" {:username uid} {$set {:first_name first-name :last_name last-name :email email}}))

(defn delete-user-by-username [uid]
  (mc/remove db "users" {:username uid}))

(defn username-not-exists? [username]
   (let [number-of-users (mc/find-maps db "users" {:username username})]
    (= (count number-of-users) 0)))

(defn valid-user? [username password]
  (let [number-of-users (mc/find-maps db "users" {:username username :password password})]
    (= (count number-of-users) 1)))

(defn user-is-admin? [uid]
  (let [number-of-users (mc/find-maps db "users" {:username uid :isAdmin "true"})]
    (= (count number-of-users) 1)))

(defn init-db []
  (if (empty? (get-all-users))
    (insert-init-users)))