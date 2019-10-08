(ns dojo.db
  (:require
   [com.stuartsierra.component :as component]
   [datomic.api :as d]))

(def uri "datomic:mem:/dojo")

(def show-schema
  [{:db/ident       :show/name
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one}

   {:db/ident       :show/date
    :db/valueType   :db.type/instant
    :db/cardinality :db.cardinality/one}

   {:db/ident       :show/id
    :db/valueType   :db.type/long
    :db/cardinality :db.cardinality/one
    :db/unique      :db.unique/identity}

   {:db/ident :show/set
    :db/valueType :db.type/ref
    :db/isComponent true
    :db/cardinality :db.cardinality/many}])

(def set-schema
  [{:db/ident :set/stage
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/one}

   {:db/ident :set/band
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/one}

   {:db/ident :set/start
    :db/valueType :db.type/instant
    :db/cardinality :db.cardinality/one}])

(def band-schema
  [{:db/ident :band/name
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one}])

(def stage-schema
  [{:db/ident :stage/name
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one}])

(def schema
  (concat show-schema
          set-schema
          band-schema
          stage-schema))

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
