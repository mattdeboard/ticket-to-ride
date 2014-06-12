(ns ttr.cards-test
  (:require [clojure.test :refer :all]
            [ttr.cards :refer :all]))

(deftest test-build-color-deck
  (let [red-trains (first (filter (fn [x] (= (:color x) :red)) trains))]
    (is (= (:count red-trains) (count (build-color-deck red-trains))))))

(deftest test-draw
  (let [deck (:cards train-deck)
        initial-ct (count @deck)
        new-deck (draw 3 train-deck)]
    (is (= (- initial-ct 3) (count @deck)))))



