(ns ttr.core
  (:require [ttr.board :as tb]
            [ttr.cards :as tc]
            [ttr.players :as tp]))

(defn start
  [player-data]
  (let [players (map #(apply tp/new-player %) player-data)]
  (do
    (tc/build-decks)
    (tc/deal-trains players))
  players))
