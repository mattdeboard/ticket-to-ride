(ns ttr.utils-test
  (:require [ttr.players :refer :all]
            [ttr.cards :refer :all]))

(defn test-player
  []
  (let [color (rand-nth ["blue" "black" "red" "yellow" "white"])
        pname (rand-nth ["Jim" "Matt" "Emma" "Ani" "Tino"])
        p (new-player pname color)]
    (->> p
         vector
         deal-trains
         deal-dests
         first)))
