(ns dojo.core
  (:require
   [com.stuartsierra.component :as component]
   [dojo.server :as server]
   [dojo.db :as db]))

(defonce system (atom nil))

(def system-map
  (component/system-map
   :db (db/new-datomic)
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

  @(d/transact
    conn
    [{:show/id 1
      :show/name "Rock in Rio"
      :show/date #inst "2019-01"
      :show/set {:set/stage "mundo"
                 :set/band "secos"
                 :set/start #inst "2019-01-01T18:00"}}

     {:db/id "mundo"
      :stage/name "Palco mundo"}

     {:db/id "secos"
      :band/name "Secos e molhados"}])

  )

(comment

  {:person/name "fulano"
   :person/age 30}

  {:person/name "ciclano"
   :person/age 10
   :person/parent 123}

  {:db/ident :person/parent
   :db/valueType :db.type/ref
   :db/cardinality :db.cardinality/many}


  [[123 :person/name "fulano" 456 true]
   [123 :person/age 30 456 true]

   [321 :person/name "ciclano" 789 true]
   [321 :person/age 10 789 true]
   [321 :person/parent 123 789 true]]


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

  (d/q '{:find  [(pull ?e [:show/name
                           :show/id
                           {:show/set
                            [{:set/band [:band/name]}]}])
                 ?inst]
         :in    [$ ?name]
         :where [[?e :show/name ?name ?tx]
                 [?tx :db/txInstant ?inst]]}
       (d/db conn)
       "Rock in Rio")

  )
