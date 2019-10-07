(ns dojo.core
  (:require
   [com.stuartsierra.component :as component]
   [datomic.api :as d]))

(def uri "datomic:mem:/dojo")

{:show/name "Rock in Rio"
 :show/date #inst "2019-01-01"}

(def schema
  [{:db/ident :show/name
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one}

   {:db/ident :show/date
    :db/valueType :db.type/instant
    :db/cardinality :db.cardinality/one}

   {:db/ident :show/id
    :db/valueType :db.type/long
    :db/cardinality :db.cardinality/one
    :db/unique :db.unique/identity}])

(defrecord Datomic [uri schema conn]
  component/Lifecycle
  (start [this]
    (d/create-database uri)
    (assoc this :conn (d/connect uri)))
  (stop [this]
    (d/delete-database uri)))

(comment

  (d/delete-database uri)

  (d/transact conn schema)

  (d/transact conn
              [{;; :show/id 1
                :show/name "Rock in Rio"
                :show/date #inst "2019-01-01"
                :foo/bar 123}])


  [id attr value tx valid?]

  [[123 :show/name "Rock in Rio" 456 true]
   [123 :show/date #inst "2019-01-01" 456 true]
   ;; [456 :db/txInstant #inst "" ,,, ,,,]
   [123 :show/name "asdasd" 678 ]]


  (d/q '{:find [?e]
         :in [$ ?name]
         :where [[?e :show/name ?name]]}
       (-> (d/db conn)
           (d/as-of #inst "2019-01-01"))
       "Rock in Rio")

  (d/q '{:find [?e1 ?e2]
         :in [$ ?name]
         :where [[?e1 :show/name ?name]
                 [?e2 :show/name ?name]]}
       (d/db conn)
       "Rock in Rio")


  (d/q '{:find [(pull ?e [:show/name :show/id])]
         :in [$ $other-db ?name]
         :where [[?e :show/name ?name]]}
       (d/db conn)
       "Rock in Rio")

  )
