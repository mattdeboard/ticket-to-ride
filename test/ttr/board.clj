(ns ttr.board-test
  (:require [clojure.test :refer :all]
            [ttr.board :refer :all]
            [ttr.test-utils :refer :all]))

(deftest test-claim-route
  (testing "Test the claim-route function."
    (deftest test-claim-route-ok
      (let [edge (first edges)
            {:keys [cost color]} edge
            player (test-player)
            pdeck (dosync
                   (ref-set (:cards (:deck player))
                            (repeat cost (keyword color))))]
        (is (= {:ok player} (claim-route player edge)))
        (dosync (alter (:state edge) merge {:claimed false :by nil}))))

    (deftest test-claim-route-claimed
      (let [edge (first edges)
            {:keys [cost color]} edge
            player (test-player)
            pdeck (dosync
                   (ref-set (:cards (:deck player))
                            (repeat cost (keyword color))))]
        (dosync (alter (:state edge) merge {:claimed true}))
        (is (= {:error "That route is claimed"} (claim-route player edge)))
        (dosync (alter (:state edge) merge {:claimed false :by nil}))))

    (deftest test-claim-route-insufficient-cards
      (let [edge (first edges)
            {:keys [cost color]} edge
            player (test-player)
            pdeck (dosync
                   (ref-set (:cards (:deck player))
                            (repeat (dec cost) (keyword color))))]
        (is (= {:error (str "Insufficient " color " cards")} (claim-route player edge)))
        (dosync (alter (:state edge) merge {:claimed false :by nil}))))
    ))








