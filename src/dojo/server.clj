(ns dojo.server
  (:require
   [com.stuartsierra.component :as component]
   [io.pedestal.http :as http]
   [io.pedestal.http.route :as route]))

(defn respond-hello [request]
  {:status 200,
   :body "Hello, world!"
   :headers {"Cache-Control" "no-cache"}})

(def routes
  (route/expand-routes
   #{["/hello" :get respond-hello
      :route-name :hello]}))

(defn create-server [port]
  (http/create-server
   {::http/routes routes
    ::http/type :jetty
    ::http/port port
    ::http/join? false}))

(defonce server (atom nil))

(defrecord Server [db server port]
  component/Lifecycle
  (start [this]
    (-> this
        (assoc :server
               (http/start
                (create-server port)))))
  (stop [this]
    (http/stop server)))
