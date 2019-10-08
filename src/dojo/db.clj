(ns dojo.db
  (:require
   [com.stuartsierra.component :as component]
   [datomic.api :as d]))

(def uri "datomic:mem:/dojo")

(def schema
  [{:db/ident       :show/name
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one}

   {:db/ident       :show/date
    :db/valueType   :db.type/instant
    :db/cardinality :db.cardinality/one}

   {:db/ident       :show/id
    :db/valueType   :db.type/long
    :db/cardinality :db.cardinality/one
    :db/unique      :db.unique/identity}])

(defrecord Datomic [uri schema conn]
  component/Lifecycle
  (start [this]
    (d/create-database uri)
    (let [conn (d/connect uri)]
      (d/transact conn schema)
      (assoc this :conn conn)))
  (stop [this]
    (d/delete-database uri)))

(defn new-datomic []
  (map->Datomic {:uri uri, :schema schema}))
