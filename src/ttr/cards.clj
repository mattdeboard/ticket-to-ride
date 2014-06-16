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

(defn deck-take!
  "Remove `n' cards from `deck', returning those cards.

This is used to simulate the 'card drawing' action."
  [n deck]
  (dosync
   (let [[cards r] (split-at n (deref (:cards deck)))]
     (ref-set (:cards deck) r)
     cards)))

(defn deck-put!
  "Put `cards' at the end of `deck'.

This is used to simulate replacing cards that have been discarded. This
should not be used against `trains-deck', as there is a discrete `discard-deck'
that is used. This is not the case with `destination-deck', where the discarded
cards should be put on the 'bottom' of the deck (i.e. the end of the vector)."
  [cards deck]
  (dosync
   (alter (:cards deck) concat cards)))

(defn deck-set!
  "Set the value of ref `deck' to `cards'."
  [cards deck]
  (dosync
   (ref-set (:cards deck) cards)))

(defn complete!
  "Mark a route card as complete."
  [route player]
  (dosync
   (alter (:state route) merge {:complete true :by player})))

(defn build-decks
  "Initialize state for the train decks: face-down and face-up."
  []
  (let [cards (shuffle (mapcat build-color-deck trains))
        [face-up face-down] (split-at 5 cards)]
    (deck-set! face-down train-deck)
    (deck-set! face-up face-up-deck)
    (deck-set! [] discard-deck)
    (deck-set! (shuffle destinations) destination-deck)
    {:face-up {:type :face-up :cards face-up}
     :train {:type :train :cards face-down}
     :discard {:type :discard :cards []}
     :destination {:type :destination :cards (shuffle destinations)}}))

(defn draw
  "Return a set of cards from the given deck."
  [n deck]
  ;; TODO: Handle the rule that says you can only draw a single train card
  ;; on a turn if you draw a face-up locomotive card.
  (deck-take! n deck))

(defn discard
  "Put discarded cards into the discard pile.

There are four types of cards:

1. train card in the draw pile (:train)

2. destination card in the draw pile (:dest)

3. train card in player's hand (:ptrain)

4. destination card in the player's hand (:pdest)
"
  [cards ^clojure.lang.Keyword deck-type]
  (match deck-type
         (:or :pdest :destination) (deck-put! cards destination-deck)
         :else (deck-put! cards discard-deck)))

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
          [cards r] (split-at 4 (deref (:cards train-deck)))]
      (deck-put! cards deck)
      (deck-set! r train-deck)))
  players)

(defn partition-deck
  [deck]
  (let [size (case (:type deck)
               :destination 3
               :train 4)
        cards (:cards deck)]
    (apply interleave (partition size cards))))

(defn deal!
  [state]
  (loop [players (:players @state)
         trains (partition-deck (get-in @state [:decks :train]))
         dests (partition-deck (get-in @state [:decks :destination]))]
    (let [[name attrs] (first players)
          ptrain (get-in attrs [:routes :cards])
          pdest (get-in attrs [:destination :cards])
          train-deck (concat ptrain (take 4 trains))
          dest-deck (concat pdest (take 3 dests))
          trainr (drop 4 trains)
          destr (drop 3 dests)]
      (swap! state assoc-in [:players name :routes :cards] train-deck)
      (swap! state assoc-in [:players name :destination :cards] dest-deck)
      (swap! state assoc-in [:decks :train :cards] trainr)
      (swap! state assoc-in [:decks :destination :cards] destr)
      (if (not (empty? (rest players)))
        (recur (rest players) trainr destr)))))

(defn deal-dests
  "Deal destination cards to players."
  [players]
  (doseq [p players]
    (let [deck (:destinations p)
          cards (draw 3 destination-deck)]
      (deck-put! cards deck)))
  players)

