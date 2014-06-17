(ns ttr.core
  (:require [ttr.board :as tb]
            [ttr.cards :as tc]
            [ttr.players :as tp]))

(defn start
  "Accepts nested vectors like `[[\"Bob\" :red] [\"Alice\" :yellow]]` to boot
the game."
  [player-data]
  (let [decks (tc/build-decks)
        state (atom {:players (into {} (map tp/a-new-player player-data))
                     :decks decks
                     :routes {:claimed #{}}})]
    (tc/deal! state)
    (loop [routes tb/edges
           turns-left nil]
      )))

