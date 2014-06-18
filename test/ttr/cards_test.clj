(ns ttr.cards-test
  (:require [clojure.test :refer :all]
            [ttr.cards :refer :all]
            [ttr.players :refer :all]
            [ttr.utils-test :refer :all]))

(deftest test-build-color-deck
  (build-decks)
  (let [red-trains (first (filter (fn [x] (= (:color x) :red)) trains))]
    (is (= (:count red-trains) (count (build-color-deck red-trains))))))
