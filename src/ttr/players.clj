(ns ttr.players
  (:require [ttr.cards :refer [discard-pile]]))

(defn new-player
  "Create a hashmap for a new player."
  [name color]
  {:name name
   :deck (ref [])
   :points (ref 0)
   :pieces-count (ref 45)
   :pieces-color color})

(defn update-player!
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
    (dosync
     (ref-set deck (concat stay-cards (repeat num-left color)))
     (alter discard-pile concat discards)
     (alter pieces-count - cost)
     (alter points + score))))

(defn reset-player! [player]
  (let [{:keys [deck points pieces-count]} player]
    (dosync
     (ref-set deck [])
     (ref-set points 0)
     (ref-set pieces-count 45))))

