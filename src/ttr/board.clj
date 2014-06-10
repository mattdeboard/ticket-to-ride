(ns ttr.board
  (:require [clojure.core.match :refer [match]]
            [ttr.players :refer [update-player!]]))

(def vertices
  [
   {:name "Atlanta"}
   {:name "Boston"}
   {:name "Calgary"}
   {:name "Charleston"}
   {:name "Chicago"}
   {:name "Dallas"}
   {:name "Denver"}
   {:name "Duluth"}
   {:name "El Paso"}
   {:name "Helena"}
   {:name "Houston"}
   {:name "Kansas City"}
   {:name "Las Vegas"}
   {:name "Little Rock"}
   {:name "Los Angeles"}
   {:name "Miami"}
   {:name "Montreal"}
   {:name "Nashville"}
   {:name "New Orleans"}
   {:name "New York"}
   {:name "Oklahoma City"}
   {:name "Omaha"}
   {:name "Phoenix"}
   {:name "Pittsburgh"}
   {:name "Portland"}
   {:name "Raleigh"}
   {:name "Saint Louis"}
   {:name "Salt Lake City"}
   {:name "San Francisco"}
   {:name "Santa Fe"}
   {:name "Sault St. Marie"}
   {:name "Seattle"}
   {:name "Toronto"}
   {:name "Vancouver"}
   {:name "Washington DC"}
   {:name "Winnipeg"}
   ])

(def edges
  [
   {:a "Atlanta" :b "Charleston" :cost 2 :color "gray"
    :state (ref {:claimed false :by nil})}
   {:a "Atlanta" :b "Miami" :cost 5 :color "blue"
    :state (ref {:claimed false :by nil})}
   {:a "Atlanta" :b "Nashville" :cost 1 :color "gray"
    :state (ref {:claimed false :by nil})}
   {:a "Atlanta" :b "New Orleans" :cost 4 :color "orange"
    :state (ref {:claimed false :by nil})}
   {:a "Atlanta" :b "New Orleans" :cost 4 :color "yellow"
    :state (ref {:claimed false :by nil})}
   {:a "Atlanta" :b "Raleigh" :cost 2 :color "gray"
    :state (ref {:claimed false :by nil})}
   {:a "Boston" :b "Montreal" :cost 2 :color "gray"
    :state (ref {:claimed false :by nil})}
   {:a "Boston" :b "Montreal" :cost 2 :color "gray"
    :state (ref {:claimed false :by nil})}
   {:a "Boston" :b "New York" :cost 2 :color "red"
    :state (ref {:claimed false :by nil})}
   {:a "Boston" :b "New York" :cost 2 :color "yellow"
    :state (ref {:claimed false :by nil})}
   {:a "Calgary" :b "Helena" :cost 4 :color "gray"
    :state (ref {:claimed false :by nil})}
   {:a "Calgary" :b "Seattle" :cost 4 :color "gray"
    :state (ref {:claimed false :by nil})}
   {:a "Calgary" :b "Vancouver" :cost 3 :color "gray"
    :state (ref {:claimed false :by nil})}
   {:a "Calgary" :b "Winnipeg" :cost 6 :color "white"
    :state (ref {:claimed false :by nil})}
   {:a "Charleston" :b "Miami" :cost 4 :color "pink"
    :state (ref {:claimed false :by nil})}
   {:a "Charleston" :b "Raleigh" :cost 1 :color "gray"
    :state (ref {:claimed false :by nil})}
   {:a "Chicago" :b "Duluth" :cost 3 :color "red"
    :state (ref {:claimed false :by nil})}
   {:a "Chicago" :b "Omaha" :cost 4 :color "blue"
    :state (ref {:claimed false :by nil})}
   {:a "Chicago" :b "Pittsburgh" :cost 3 :color "black"
    :state (ref {:claimed false :by nil})}
   {:a "Chicago" :b "Pittsburgh" :cost 3 :color "orange"
    :state (ref {:claimed false :by nil})}
   {:a "Chicago" :b "Saint Louis" :cost 2 :color "green"
    :state (ref {:claimed false :by nil})}
   {:a "Chicago" :b "Saint Louis" :cost 2 :color "white"
    :state (ref {:claimed false :by nil})}
   {:a "Chicago" :b "Toronto" :cost 4 :color "white"
    :state (ref {:claimed false :by nil})}
   {:a "Dallas" :b "El Paso" :cost 4 :color "red"
    :state (ref {:claimed false :by nil})}
   {:a "Dallas" :b "Houston" :cost 1 :color "gray"
    :state (ref {:claimed false :by nil})}
   {:a "Dallas" :b "Houston" :cost 1 :color "gray"
    :state (ref {:claimed false :by nil})}
   {:a "Dallas" :b "Little Rock" :cost 2 :color "gray"
    :state (ref {:claimed false :by nil})}
   {:a "Dallas" :b "Oklahoma City" :cost 2 :color "gray"
    :state (ref {:claimed false :by nil})}
   {:a "Dallas" :b "Oklahoma City" :cost 2 :color "gray"
    :state (ref {:claimed false :by nil})}
   {:a "Denver" :b "Helena" :cost 4 :color "green"
    :state (ref {:claimed false :by nil})}
   {:a "Denver" :b "Kansas City" :cost 4 :color "black"
    :state (ref {:claimed false :by nil})}
   {:a "Denver" :b "Kansas City" :cost 4 :color "orange"
    :state (ref {:claimed false :by nil})}
   {:a "Denver" :b "Oklahoma City" :cost 4 :color "red"
    :state (ref {:claimed false :by nil})}
   {:a "Denver" :b "Omaha" :cost 4 :color "pink"
    :state (ref {:claimed false :by nil})}
   {:a "Denver" :b "Phoenix" :cost 4 :color "white"
    :state (ref {:claimed false :by nil})}
   {:a "Denver" :b "Salt Lake City" :cost 3 :color "red"
    :state (ref {:claimed false :by nil})}
   {:a "Denver" :b "Salt Lake City" :cost 3 :color "yellow"
    :state (ref {:claimed false :by nil})}
   {:a "Denver" :b "Santa Fe" :cost 2 :color "gray"
    :state (ref {:claimed false :by nil})}
   {:a "Duluth" :b "Helena" :cost 6 :color "orange"
    :state (ref {:claimed false :by nil})}
   {:a "Duluth" :b "Omaha" :cost 2 :color "gray"
    :state (ref {:claimed false :by nil})}
   {:a "Duluth" :b "Omaha" :cost 2 :color "gray"
    :state (ref {:claimed false :by nil})}
   {:a "Duluth" :b "Sault St. Marie" :cost 3 :color "gray"
    :state (ref {:claimed false :by nil})}
   {:a "Duluth" :b "Toronto" :cost 6 :color "pink"
    :state (ref {:claimed false :by nil})}
   {:a "Duluth" :b "Winnipeg" :cost 4 :color "black"
    :state (ref {:claimed false :by nil})}
   {:a "El Paso" :b "Houston" :cost 6 :color "green"
    :state (ref {:claimed false :by nil})}
   {:a "El Paso" :b "Los Angeles" :cost 6 :color "black"
    :state (ref {:claimed false :by nil})}
   {:a "El Paso" :b "Oklahoma City" :cost 5 :color "yellow"
    :state (ref {:claimed false :by nil})}
   {:a "El Paso" :b "Phoenix" :cost 3 :color "gray"
    :state (ref {:claimed false :by nil})}
   {:a "El Paso" :b "Santa Fe" :cost 2 :color "gray"
    :state (ref {:claimed false :by nil})}
   {:a "Helena" :b "Calgary" :cost 4 :color "gray"
    :state (ref {:claimed false :by nil})}
   {:a "Helena" :b "Omaha" :cost 5 :color "red"
    :state (ref {:claimed false :by nil})}
   {:a "Helena" :b "Seattle" :cost 6 :color "yellow"
    :state (ref {:claimed false :by nil})}
   {:a "Helena" :b "Winnipeg" :cost 4 :color "blue"
    :state (ref {:claimed false :by nil})}
   {:a "Houston":b "New Orleans" :cost 2 :color "gray"
    :state (ref {:claimed false :by nil})}
   {:a "Kansas City" :b "Oklahoma City" :cost 2 :color "gray"
    :state (ref {:claimed false :by nil})}
   {:a "Kansas City" :b "Oklahoma City" :cost 2 :color "gray"
    :state (ref {:claimed false :by nil})}
   {:a "Kansas City" :b "Omaha" :cost 1 :color "gray"
    :state (ref {:claimed false :by nil})}
   {:a "Kansas City" :b "Omaha" :cost 1 :color "gray"
    :state (ref {:claimed false :by nil})}
   {:a "Kansas City" :b "Saint Louis" :cost 2 :color "blue"
    :state (ref {:claimed false :by nil})}
   {:a "Kansas City" :b "Saint Louis" :cost 2 :color "pink"
    :state (ref {:claimed false :by nil})}
   {:a "Las Vegas" :b "Los Angeles" :cost 2 :color "gray"
    :state (ref {:claimed false :by nil})}
   {:a "Las Vegas" :b "Salt Lake City" :cost 3 :color "orange"
    :state (ref {:claimed false :by nil})}
   {:a "Little Rock" :b "Nashville" :cost 3 :color "white"
    :state (ref {:claimed false :by nil})}
   {:a "Little Rock" :b "New Orleans" :cost 3 :color "green"
    :state (ref {:claimed false :by nil})}
   {:a "Little Rock" :b "Oklahoma City" :cost 2 :color "gray"
    :state (ref {:claimed false :by nil})}
   {:a "Little Rock" :b "Saint Louis" :cost 2 :color "gray"
    :state (ref {:claimed false :by nil})}
   {:a "Los Angeles" :b "Phoenix" :cost 2 :color "black"
    :state (ref {:claimed false :by nil})}
   {:a "Los Angeles" :b "San Francisco" :cost 3 :color "pink"
    :state (ref {:claimed false :by nil})}
   {:a "Los Angeles" :b "San Francisco" :cost 3 :color "yellow"
    :state (ref {:claimed false :by nil})}
   {:a "Miami" :b "New Orleans" :cost 6 :color "red"
    :state (ref {:claimed false :by nil})}
   {:a "Montreal" :b "New York" :cost 3 :color "blue"
    :state (ref {:claimed false :by nil})}
   {:a "Montreal" :b "Sault St. Marie" :cost 5 :color "black"
    :state (ref {:claimed false :by nil})}
   {:a "Montreal" :b "Toronto" :cost 3 :color "gray"
    :state (ref {:claimed false :by nil})}
   {:a "Nashville" :b "Pittsburgh" :cost 4 :color "yellow"
    :state (ref {:claimed false :by nil})}
   {:a "Nashville" :b "Raleigh" :cost 3 :color "black"
    :state (ref {:claimed false :by nil})}
   {:a "Nashville" :b "Saint Louis" :cost 2 :color "gray"
    :state (ref {:claimed false :by nil})}
   {:a "New York" :b "Pittsburgh" :cost 2 :color "green"
    :state (ref {:claimed false :by nil})}
   {:a "New York" :b "Pittsburgh" :cost 2 :color "white"
    :state (ref {:claimed false :by nil})}
   {:a "New York" :b "Washington DC" :cost 2 :color "black"
    :state (ref {:claimed false :by nil})}
   {:a "New York" :b "Washington DC" :cost 2 :color "orange"
    :state (ref {:claimed false :by nil})}
   {:a "Phoenix" :b "Santa Fe" :cost 3 :color "gray"
    :state (ref {:claimed false :by nil})}
   {:a "Pittsburgh" :b "Saint Louis" :cost 5 :color "green"
    :state (ref {:claimed false :by nil})}
   {:a "Portland" :b "Salt Lake City" :cost 6 :color "blue"
    :state (ref {:claimed false :by nil})}
   {:a "Portland" :b "San Francisco" :cost 5 :color "green"
    :state (ref {:claimed false :by nil})}
   {:a "Portland" :b "San Francisco" :cost 5 :color "pink"
    :state (ref {:claimed false :by nil})}
   {:a "Portland" :b "Seattle" :cost 1 :color "gray"
    :state (ref {:claimed false :by nil})}
   {:a "Portland" :b "Seattle" :cost 1 :color "gray"
    :state (ref {:claimed false :by nil})}
   {:a "Raleigh" :b "Washington DC" :cost 2 :color "gray"
    :state (ref {:claimed false :by nil})}
   {:a "Saint Louis" :b "" :cost 2 :color "gray"
    :state (ref {:claimed false :by nil})}
   {:a "Salt Lake City" :b "San Francisco" :cost 5 :color "orange"
    :state (ref {:claimed false :by nil})}
   {:a "Salt Lake City" :b "San Francisco" :cost 5 :color "white"
    :state (ref {:claimed false :by nil})}
   {:a "Sault St. Marie" :b "Toronto" :cost 2 :color "gray"
    :state (ref {:claimed false :by nil})}
   {:a "Sault St. Marie" :b "Winnipeg" :cost 6 :color "gray"
    :state (ref {:claimed false :by nil})}
   {:a "Seattle" :b "Vancouver" :cost 1 :color "gray"
    :state (ref {:claimed false :by nil})}
   ])

(def scoring
  {1 1
   2 2
   3 4
   4 7
   5 10
   6 15})

(defn select-vals
  "Returns the values for the keys in the collection `ks'.

Example:
  ;; returns `'(1 2)`
  (let [m {:a 1 :b 2 :c 3}]
    (select-vals m [:a :b]))
"
  [m ks]
  (-> m (select-keys ks) vals))

(defn group-by-many
  "Like `group-by', but allows grouping by multiple keys.

Example:

  (group-by-many [{:a 1 :b 2 :c 3} {:a 7 :b 12 :c -4}] [:a :c])
  >>> {#{1 3} {:a 1 :b 2 :c 3} #{7 -4} {:a 7 :b 12 :c -4}}
"
  [m ks]
  (let [f (fn [x]
            {(-> x (select-keys ks) vals set) x})]
    (apply merge (map f m))))

;; Create an "index" of the edges/routes, so that looking up a particular
;; route is easy.
(def edges-index (delay (group-by-many edges [:a :b :color])))

(defn reset-route!
  "Reset the state of the route to unclaimed."
  [route]
  (dosync
   (ref-set (:state route) {:claimed false :by nil})))

(defn get-route
  "Get a route (defined in `edges' above) from the index of edges.

`k' is a set containing the values of the :a, :b, and :color keys. Example:

  ;; returns {:a \"Seattle\" :b \"Vancouver\" :cost 1 :color \"gray\"
  ;;          :state (ref {:claimed false :by nil})}
  (get-route #{\"Seattle\" \"gray\" \"Vancouver\"})
"
  [k]
  (get @edges-index k))

(defn claim-route
  "Claim a particular route for a player.

There is a lot going on here, and could probably use a refactor. That said,
here is a rundown:

  - find the colors from the player's deck that could concievably be used to
claim this route. It seemed like the right thing to do was to group the player's
hand by card color. Then, count each color.

  - of the card groups (e.g. `[[:red :red :red] [:blue :blue]]` that exist in
sufficient numbers in the player's hand, find which ones can meet the cost of
the route. If the route color is 'gray', this is all we have to do. Naively, I'm
taking the first matching color group and removing those from the player's hand.

  - If the route does have a color, it pretty much works the same, logically.
(That's what the `or` function is doing in the `fn` for `filterv`.) This way
there's no crazy special-casing to handle gray. Well... there is, but it's
pretty localized.

  - check to ensure the player has enough train cars left to make this move

  - check to ensure the route isn't already claimed

If all this turns out in the player's favor, update the state of the route to
'claimed', and finally toss the player up to `update-player!` to update the
player's state."
  [player route]
  (let [{:keys [color cost state]} route
        {:keys [deck pieces-count]} player
        in-hand (count (filter #{color} @deck))
        valid-colors (map #(keyword (first %))
                          (filterv
                           (fn [g]
                             (and
                              ;; TODO: Count prismatic cards toward the count
                              ;; here since they stand in for any color.
                              (>= (apply count (rest g)) cost)
                              (or (= (name (first g)) color)
                                  (= "gray" color))))
                           (group-by identity @deck)))]
    (match [(empty? valid-colors)
            (>= @pieces-count cost)
            (not (:claimed @state))]
           [false true true]
           (do
             (dosync
              (ref-set state {:claimed true :by player}))
             (update-player! player (first valid-colors) cost (get scoring cost))
             {:ok player})
           [true _ _] {:error (str "Insufficient " color " cards")}
           [_ false _] {:error "Insufficient train cars"}
           [_ _ false] {:error "That route is claimed"})))

