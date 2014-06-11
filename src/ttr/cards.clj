(ns ttr.cards
  (:require [ttr.shuffle :refer :all]))

;; All the destination cards, their start/end points, and the point
;; value of each.
(def destinations
  [{:a "Dallas" :b "New York" :points 11
    :state (ref {:completed false :by nil})}
   {:a "Portland" :b "Phoenix" :points 11
    :state (ref {:completed false :by nil})}
   {:a "Vancouver" :b "Santa Fe" :points 13
    :state (ref {:completed false :by nil})}
   {:a "Seattle" :b "New York" :points 22
    :state (ref {:completed false :by nil})}
   {:a "Montreal" :b "Atlanta" :points 9
    :state (ref {:completed false :by nil})}
   {:a "Toronto" :b"Miami" :points 10
    :state (ref {:completed false :by nil})}
   {:a "Boston" :b "Miami" :points 12
    :state (ref {:completed false :by nil})}
   {:a "Los Angeles" :b"Chicago" :points 16
    :state (ref {:completed false :by nil})}
   {:a "Winnipeg" :b"Houston" :points 12
    :state (ref {:completed false :by nil})}
   {:a "Denver" :b"El Paso" :points 4
    :state (ref {:completed false :by nil})}
   {:a "Duluth" :b"Houston" :points 8
    :state (ref {:completed false :by nil})}
   {:a "San Francisco" :b"Atlanta" :points 17
    :state (ref {:completed false :by nil})}
   {:a "Denver" :b"Pittsburgh" :points 11
    :state (ref {:completed false :by nil})}
   {:a "Sault St. Marie" :b"Nashville" :points 8
    :state (ref {:completed false :by nil})}
   {:a "Winnipeg" :b"Little Rock" :points 11
    :state (ref {:completed false :by nil})}
   {:a "Duluth" :b"El Paso" :points 10
    :state (ref {:completed false :by nil})}
   {:a "Seattle" :b"Los Angeles" :points 9
    :state (ref {:completed false :by nil})}
   {:a "Helena" :b"Los Angeles" :points 8
    :state (ref {:completed false :by nil})}
   {:a "Kansas City" :b"Houston" :points 5
    :state (ref {:completed false :by nil})}
   {:a "Sault St. Marie" :b"Oklahoma City" :points 9
    :state (ref {:completed false :by nil})}
   {:a "Portland" :b"Nashville" :points 17
    :state (ref {:completed false :by nil})}
   {:a "Los Angeles" :b"New York" :points 21
    :state (ref {:completed false :by nil})}
   {:a "Chicago" :b"Santa Fe" :points 9
    :state (ref {:completed false :by nil})}
   {:a "Calgary" :b"Phoenix" :points 13
    :state (ref {:completed false :by nil})}
   {:a "Calgary" :b"Salt Lake City" :points 7
    :state (ref {:completed false :by nil})}
   {:a "Vancouver" :b"Montreal" :points 20
    :state (ref {:completed false :by nil})}
   {:a "Los Angeles" :b"Miami" :points 20
    :state (ref {:completed false :by nil})}
   {:a "Chicago" :b"New Orleans" :points 7
    :state (ref {:completed false :by nil})}
   {:a "New York" :b"Atlanta" :points 6
    :state (ref {:completed false :by nil})}
   {:a "Montreal" :b"New Orleans" :points 13
    :state (ref {:completed false :by nil})}])

(defn deck-take!
  "Remove `n' cards from `deck', returning those cards.

This is used to simulate the 'card drawing' action."
  [n deck]
  (dosync
   (let [[cards r] (split-at n @deck)]
     (ref-set deck r)
     cards)))

(defn deck-put!
  "Put `cards' at the end of `deck'.

This is used to simulate replacing cards that have been discarded. This
should not be used against `trains-deck', as there is a discrete `discard-deck'
that is used. This is not the case with `destination-deck', where the discarded
cards should be put on the 'bottom' of the deck (i.e. the end of the vector)."
  [cards deck]
  (dosync
   (alter deck concat cards)))

(defn deck-set!
  "Set the value of ref `deck' to `cards'."
  [cards deck]
  (dosync
   (ref-set deck cards)))

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

(def destination-deck (ref (shuffle destinations)))
(def discard-deck (ref []))
(def face-up-deck (ref []))
(def train-deck (ref []))

(defn build-decks
  "Initialize state for the train decks: face-down and face-up."
  []
  (let [cards (shuffle (mapcat build-color-deck trains))
        [face-up face-down] (split-at 5 cards)]
    (deck-set! face-down train-deck)
    (deck-set! face-up face-up-deck)))

(defn deal-trains
  "Perform initial deal of train cards.

This function splits the deck of train cards into n decks of 4 cards,
where n is the number of players. Each player's deck state is updated with
the values from one of the stacks."
  [players]
  ;; TODO: Look into whether `take-nth` might not be better here, and hew
  ;; closer to real-world shufflin'.
  (doseq [p players]
    (let [deck (:deck p)
          [cards r] (split-at 4 @train-deck)]
      (deck-put! cards deck)
      (deck-set! r train-deck))))

(defn draw
  "Return a set of cards from the given deck."
  [n deck]
  ;; TODO: Handle the rule that says you can only draw a single train card
  ;; on a turn if you draw a face-up locomotive card.
  (deck-take! n deck))
