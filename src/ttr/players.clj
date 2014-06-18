(ns ttr.players
  (:require [ttr.cards :refer :all]))

;; Each train color has 45 pieces.
(def starting-pcs 45)

(defn a-new-player
  [[name color]]
  {name {:routes {:cards [] :claimed []}
         :destinations {:cards [] :completed []}
         :score 0
         :pieces {:color color :count 45}}})
