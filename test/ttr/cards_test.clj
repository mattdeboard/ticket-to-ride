(ns ttr.cards-test
  (:require [clojure.test :refer :all]
            [ttr.cards :refer :all]
            [ttr.players :refer :all]
            [ttr.utils-test :refer :all]))

(deftest test-build-color-deck
  (build-decks)
  (let [red-trains (first (filter (fn [x] (= (:color x) :red)) trains))]
    (is (= (:count red-trains) (count (build-color-deck red-trains))))))

(deftest test-draw
  (build-decks)
  (let [deck (:cards train-deck)
        initial-ct (count @deck)
        new-deck (draw 3 train-deck)]
    (is (= (- initial-ct 3) (count @deck)))))

(deftest test-discard
  (build-decks)
  (let [player (->> (test-player)
                    vector
                    deal-trains
                    deal-dests
                    first)
        deck (-> player :deck :cards)
        dtype (-> player :deck :type)
        discards (take 2 @deck)
        keepers (drop 2 @deck)
        _ (discard discards dtype)]
    (is (= (deref (:cards discard-deck)) discards))))
