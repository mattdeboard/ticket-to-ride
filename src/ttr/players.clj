(ns ttr.players
  (:require [ttr.cards :refer :all]))

;; Each train color has 45 pieces.
(def starting-pcs 45)

(defn new-player
  "Create a hashmap for a new player."
  [name color]
  {:name name
   :deck {:type :ptrain :cards (ref [])}
   :destinations {:type :pdest :cards (ref [])}
   :points (ref 0)
   :pieces-count (ref starting-pcs)
   :pieces-color color})

(defn rinc!
  "Increase the value of a ref, which is an element in hash map `player'
with key `k' with a numerical value."
  [n player ^clojure.lang.Keyword k]
  (dosync
   (let [s (get player k)]
     (alter s + n))))

(defn rdec!
  "Decrease the value of a ref, which is an element in hash map `player'
with key `k', with a numerical value."
  [n player ^clojure.lang.Keyword k]
  (dosync
   (let [s (get player k)]
     (alter s - n))))

(defn rset!
  "Set the value of a ref, which is an element in hash map `player'
with key `k', to value `v'."
  [v player ^clojure.lang.Keyword k]
  (dosync
   (let [s (get player k)]
     (ref-set s v))))

(defn update-player
  "Update player state after an action.

This includes decrementing their deck & train set for any moves made, as well
as incrementing their score.

Also, the discard pile is updated to include any newly used cards."
  [player color cost score]
  (let [{:keys [deck pieces-count points]} player
        cards (:cards deck)
        in-hand (count (filter #{color} @cards))
        ;; separate out the cards of target color from others
        stay-cards (->> @cards
                        (group-by identity)
                        (#(dissoc % color))
                        vals
                        (apply concat))
        discards (filter #(= color %) @cards)
        num-left (- in-hand cost)]
    (do
      (deck-set! (concat stay-cards (repeat num-left color)) deck)
      (deck-put! discards discard-deck)
      (rinc! score player :points)
      (rdec! cost player :pieces-count))))

(defn reset-player
  "A convenience function to put the given player's stateful attributes back
to their original state."
  [player]
  (let [{:keys [deck points pieces-count destinations]} player]
    (do
      (deck-set! [] deck)
      (deck-set! [] destinations)
      (rset! :points 0 player)
      (rset! :pieces-count starting-pcs))))
