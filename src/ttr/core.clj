(ns ttr.core
  (:require [ttr.board :as tb]
            [ttr.cards :as tc]
            [ttr.players :as tp]))

(defn start
  [player-data]
  (tc/build-decks)
  (let [players (map #(apply tp/new-player %) player-data)]
    (do
      (tc/deal-trains players)
      (tc/deal-dests players))
    players))
