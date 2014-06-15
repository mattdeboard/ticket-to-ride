(ns ttr.board-test
  (:require [clojure.test :refer :all]
            [ttr.board :refer :all]
            [ttr.cards :refer :all]
            [ttr.test-utils :refer :all]))

(deftest test-claim-route-ok
  (testing "The claim-route happy path: The route is unclaimed and the
player has sufficient cards & trains."
    (let [edge {:a "Boston" :b "New York" :cost 5 :color :red
                :state (ref {:claimed false :by nil})}
          {:keys [cost color]} edge
          player (test-player)
          deck (:deck player)
          pdeck (deck-set! (repeat cost (keyword color)) deck)]
      (is (= {:ok player} (claim-route player edge)))
      (reset-route! edge))))

(deftest test-claim-route-prismatic
  (testing "Ensure prismatic cards are counted when checking whether a route
can be claimed."
    (let [edge {:a "Boston" :b "New York" :cost 5 :color :red
                :state (ref {:claimed false :by nil})}
          {:keys [cost color]} edge
          player (test-player)
          deck (:deck player)
          pdeck (deck-put! (concat (repeat (- cost 2) color)
                                   (repeat 2 :prismatic))
                           deck)]
      (is (= {:ok player} (claim-route player edge)))
      (reset-route! edge))))

(deftest test-claim-route-claimed
  (testing "Test to ensure a route marked as claimed cannot be claimed
again."
    (let [edge {:a "Boston" :b "New York" :cost 5 :color :red
                :state (ref {:claimed false :by nil})}
          {:keys [cost color]} edge
          player (test-player)
          deck (:deck player)
          pdeck (deck-set! (repeat cost (keyword color)) deck)]
      (claim-route! edge player)
      (is (= {:error "That route is claimed"} (claim-route player edge)))
      (reset-route! edge))))

(deftest test-claim-route-insufficient-cards
  (testing "Test to ensure a route will not be claimed without the right
quantity of the right cards."
    (let [edge {:a "Boston" :b "New York" :cost 5 :color :red
                :state (ref {:claimed false :by nil})}
          {:keys [cost color]} edge
          player (test-player)
          deck (:deck player)
          pdeck (deck-set! (repeat (dec cost) (keyword color)) deck)]
      (is (= {:error (str "Insufficient " color " cards")}
             (claim-route player edge)))
      (reset-route! edge))))
