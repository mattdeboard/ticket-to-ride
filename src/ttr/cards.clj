(ns ttr.cards
  (:require [clojure.core.match :refer [match]]))

;; All the destination cards, their start/end points, and the point
;; value of each.
(def destinations
  [{:a "Dallas" :b "New York" :points 11}
   {:a "Portland" :b "Phoenix" :points 11}
   {:a "Vancouver" :b "Santa Fe" :points 13}
   {:a "Seattle" :b "New York" :points 22}
   {:a "Montreal" :b "Atlanta" :points 9}
   {:a "Toronto" :b"Miami" :points 10}
   {:a "Boston" :b "Miami" :points 12}
   {:a "Los Angeles" :b"Chicago" :points 16}
   {:a "Winnipeg" :b"Houston" :points 12}
   {:a "Denver" :b"El Paso" :points 4}
   {:a "Duluth" :b"Houston" :points 8}
   {:a "San Francisco" :b"Atlanta" :points 17}
   {:a "Denver" :b"Pittsburgh" :points 11}
   {:a "Sault St. Marie" :b"Nashville" :points 8}
   {:a "Winnipeg" :b"Little Rock" :points 11}
   {:a "Duluth" :b"El Paso" :points 10}
   {:a "Seattle" :b"Los Angeles" :points 9}
   {:a "Helena" :b"Los Angeles" :points 8}
   {:a "Kansas City" :b"Houston" :points 5}
   {:a "Sault St. Marie" :b"Oklahoma City" :points 9}
   {:a "Portland" :b"Nashville" :points 17}
   {:a "Los Angeles" :b"New York" :points 21}
   {:a "Chicago" :b"Santa Fe" :points 9}
   {:a "Calgary" :b"Phoenix" :points 13}
   {:a "Calgary" :b"Salt Lake City" :points 7}
   {:a "Vancouver" :b"Montreal" :points 20}
   {:a "Los Angeles" :b"Miami" :points 20}
   {:a "Chicago" :b"New Orleans" :points 7}
   {:a "New York" :b"Atlanta" :points 6}
   {:a "Montreal" :b"New Orleans" :points 13}])

;; Data about the train cards. This is used to build the communal deck of
;; train cards.
(def trains
  [
   {:color :red :count 12}
   {:color :orange :count 12}
   {:color :yellow :count 12}
   {:color :green :count 12}
   {:color :blue :count 12}
   {:color :black :count 12}
   {:color :white :count 12}
   {:color :pink :count 12}
   {:color :prismatic :count 14}
   ])

(defn build-color-deck
  "Use the information in the `trains' vector to create a collection
of cards of a single color ('prismatic' being a color)."
  [train]
  (repeat (:count train) (:color train)))

(def destination-deck {:type :destination :cards (ref (shuffle destinations))})
(def discard-deck {:type :discard :cards (ref [])})
(def face-up-deck {:type :face-up :cards (ref [])})
(def train-deck {:type :train :cards (ref [])})

(defn build-decks
  "Initialize state for the train decks: face-down and face-up."
  []
  (let [cards (shuffle (mapcat build-color-deck trains))
        [face-up face-down] (split-at 5 cards)]
    {:face-up {:type :face-up :cards face-up}
     :train {:type :train :cards face-down}
     :discard {:type :discard :cards []}
     :destination {:type :destination
                   :cards (shuffle destinations)
                   :completed #{}}}))
