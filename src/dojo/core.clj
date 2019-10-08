(ns dojo.core
  (:require
   [com.stuartsierra.component :as component]
   [dojo.server :as server]
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

(defonce system (atom nil))

(def system-map
  (component/system-map
   :db (map->Datomic {:uri uri, :schema schema})
   :server (-> (server/map->Server {:port 8080})
               (component/using [:db]))))

(defn start []
  (reset! system (component/start-system system-map)))

(defn stop []
  (swap! system component/stop-system))


(comment
  ;; dev

  (def conn (:conn (:db @system)))

  (start)

  (stop)

  (deref system)

  (d/transact conn schema)

  (d/transact conn
              [{;; :show/id 1
                :show/name "Rock in Rio"
                :show/date #inst "2019-01-01"}])
  )

(comment
  ;; query

  (d/q '{:find  [?e]
         :in    [$ ?name]
         :where [[?e :show/name ?name]]}
       (-> (d/db conn)
           #_(d/as-of #inst "2019-01-01"))
       "Rock in Rio")

  (d/q '{:find  [?e1 ?e2]
         :in    [$ ?name]
         :where [[?e1 :show/name ?name]
                 [?e2 :show/name ?name]]}
       (d/db conn)
       "Rock in Rio")

  (d/q '{:find  [(pull ?e [:show/name :show/id])]
         :in    [$ $other-db ?name]
         :where [[?e :show/name ?name]]}
       (d/db conn)
       "Rock in Rio"))
