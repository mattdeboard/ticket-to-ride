(ns ttr.board-test
  (:require [clojure.test :refer :all]
            [ttr.board :refer :all]
            [ttr.cards :refer :all]
            [ttr.core :refer :all]
            [ttr.utils-test :refer :all]))

(deftest test-claim!-ok
  (testing "The claim-route happy path: The route is unclaimed and the
player has sufficient cards & trains."
    (let [pname "Jim"
          state (start [[pname :blue]])
          route {:a "Boston" :b "New York" :cost 2 :color :red}]
      (swap! state assoc-in [:players pname :routes :cards]
             (repeat (:cost route) (:color route)))
      (is (= {:ok (get (:players (deref state)) pname)}
             (claim! state pname route))))))

(deftest text-claim!-prismatic
  (testing "Ensure prismatic cards are counted when checking whether a route
can be claimed."
    (let [pname "Jim"
          state (start [[pname :blue]])
          route {:a "Boston" :b "New York" :cost 2 :color :red}
          {:keys [cost color]} route]
      (swap! state assoc-in [:players pname :routes :cards]
             (concat (repeat (- cost 1) color)
                     (repeat 1 :prismatic)))
      (is (= {:ok (get-in @state [:players pname])}
             (claim! state pname route))))))

(deftest test-claim!-claimed
  (testing "Test to ensure a route marked as claimed cannot be claimed
again."
    (let [jim "Jim"
          betty "Betty"
          state (start [[jim :blue] [betty :yellow]])
          route {:a "Boston" :b "New York" :cost 2 :color :red}
          {:keys [cost color]} route
          _ (swap! state assoc-in [:players jim :routes :cards]
                   (repeat cost color))
          player (get-in @state [:players jim])]
      (swap! state assoc-in [:players betty :routes :cards] (repeat cost color))
      (claim! state betty route)
      (is (= {:error "That route is claimed"} (claim! state jim route))))))

(deftest test-claim!-insufficient-cards
  (testing "Test to ensure a route will not be claimed without the right
quantity of the right cards."
    (let [pname "Jim"
          state (start [[pname :blue]])
          route {:a "Boston" :b "New York" :cost 2 :color :red}
          {:keys [cost color]} route]
      (swap! state assoc-in [:players pname :routes :cards]
             (repeat (dec cost) color))
      (is (= {:error (str "Insufficient " (name color) " cards")}
             (claim! state pname route))))))
