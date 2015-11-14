(ns webhookproxyweb.domain.users
  (:require [yesql.core :refer [defquery]]
            [clj-uuid :as uuid]
            [webhookproxyweb.db :refer [using-db]]
            [webhookproxyweb.external.github :as github]))


(defrecord Users [db gh])

(defquery insert-user<! "sql/user-insert.sql")
(defquery get-user "sql/user-get.sql")


(declare find-by add)

(defn github-enrollment-and-identify [users code]
  (let [result (github/authenticate (:gh users) code)
        github-id (-> result :identity :id)
        github-email (-> result :identity :email)
        existing-user (find-by users :github-id github-id)
        user (or (first existing-user) 
                 (add users {:provider "github"
                             :uid github-id
                             :email github-email }))
        user-id (:id user)]
    user-id))

    
(defmulti find-by (fn [users & args] (first args)))

(defmethod find-by :github-id [{:keys [db]} _ uid]
  (using-db db get-user {:uid (str uid)
                         :provider "github" }))

(defn- add [{:keys [db]} {:keys [provider uid email] }]
  (using-db db insert-user<! 
            {:id (uuid/v4)
             :provider provider
             :uid (str uid)
             :email email }))
