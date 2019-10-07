(defproject dojo "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.10.1"]

                 ;; config
                 [aero "1.1.3"]

                 ;; pedestal
                 [io.pedestal/pedestal.service       "0.5.7"]
                 [io.pedestal/pedestal.service-tools "0.5.7"] ;; Only needed for ns-watching; WAR tooling
                 [io.pedestal/pedestal.jetty         "0.5.7"]

                 ;; logging
                 [org.slf4j/slf4j-simple "1.7.28"]
                 [org.clojure/tools.logging "0.4.1" :exclusions [org.slf4j/slf4j-nop]]

                 ;; db
                 [com.datomic/datomic-free "0.9.5697"]
                 [org.clojure/tools.cli "0.3.6"]
                 [juxt/crux-core "19.09-1.4.0-alpha"]

                 ;; kafka
                 [fundingcircle/jackdaw "0.6.8"]
                 [willa "0.1.0"]

                 ;; generative testing
                 [org.clojure/test.check "0.10.0"]
                 [com.gfredericks/test.chuck "0.2.10"]

                 ;; test runner
                 [lambdaisland/kaocha "0.0-554"]

                 ;; component
                 [com.stuartsierra/component "0.4.0"]
                 ;; https://github.com/weavejester/reloaded.repl
                 [reloaded.repl "0.2.4"]
                 ;; https://github.com/danielsz/system
                 [org.danielsz/system "0.4.3"]

                 ;; integrant, a component alternative
                 ;; https://github.com/weavejester/integrant
                 [integrant "0.7.0"]
                 ;; https://github.com/weavejester/integrant-repl
                 [integrant/repl "0.3.1"]

                 ;;utils
                 ;;https://github.com/weavejester/medley
                 [medley "1.1.0"]
                 [org.clojure/math.combinatorics "0.1.6"]]

  :aliases {"kaocha" ["run" "-m" "kaocha.runner"]})
