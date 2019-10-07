(ns dojo.core-test
  (:require [clojure.test :refer [deftest is run-tests]]))

(deftest some-test
  (is (= true true)))










(comment


  (run-tests)

  (defn convert [k v])

  (def vamp-keys [])

  (defn mapify [rows]
    (map (fn [unmapped-row]
           (reduce (fn [row-map [vamp-key value]]
                     (assoc row-map vamp-key (convert vamp-key value)))
                   {}
                   (map vector vamp-keys unmapped-row)))
         rows))


  (defn mapify [rows]
    (->> (map (partial zipmap vamp-keys) rows)
         (map #(reduce-kv (fn [row k v]
                            (assoc row k (convert k v)))
                          {}, %)
              ,,,)))

  (zipmap [:foo :bar :baz] [1 2 3])

  ({:foo 1, :bar 2, :baz 3}
   {:foo 4, :bar 5, :baz 6}
   {:foo 6, :bar 7, :baz 8})

  (map (partial zipmap [:foo :bar :baz])
       [[1 2 3] [4 5 6] [6 7 8]])

  (map inc [1 2 3])
  )
