(ns ttr.players
  (:require [ttr.cards :refer :all]))

(def starting-pcs 45)

(defn new-player
  "Create a hashmap for a new player."
  [name color]
  {:name name
   :deck (ref [])
   :destinations (ref [])
   :points (ref 0)
   :pieces-count (ref starting-pcs)
   :pieces-color color})

(defn ref-inc!
  "Increase the value of a ref, which is an element in hash map `player'
with key `k' with a numerical value."
  [n player ^clojure.lang.Keyword k]
  (dosync
   (let [s (get player k)]
     (alter s + n))))

(defn ref-dec!
  "Decrease the value of a ref, which is an element in hash map `player'
with key `k', with a numerical value."
  [n player ^clojure.lang.Keyword k]
  (dosync
   (let [s (get player k)]
     (alter s - n))))

(defn ref-set!
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
        in-hand (count (filter #{color} @deck))
        ;; separate out the cards of target color from others
        stay-cards (->> @deck
                        (group-by identity)
                        (#(dissoc % color))
                        vals
                        (apply concat))
        discards (filter #(= color %) @deck)
        num-left (- in-hand cost)]
    (do
      (deck-set! deck (concat stay-cards (repeat num-left color)))
      (deck-put! discard-deck discards)
      (ref-inc! score player :points)
      (ref-dec! cost player :pieces-count))))

(defn reset-player
  [player]
  (let [{:keys [deck points pieces-count destinations]} player]
    (do
      (deck-set! [] deck)
      (deck-set! [] destinations)
      (ref-set! :points 0 player)
      (ref-set! :pieces-count starting-pcs))))
