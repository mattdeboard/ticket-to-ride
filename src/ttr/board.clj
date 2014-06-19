(ns ttr.board
  (:require [clojure.core.match :refer [match]]))

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
    {:a "Atlanta" :b "Charleston" :cost 2 :color :gray}
    {:a "Atlanta" :b "Miami" :cost 5 :color :blue}
    {:a "Atlanta" :b "Nashville" :cost 1 :color :gray}
    {:a "Atlanta" :b "New Orleans" :cost 4 :color :orange}
    {:a "Atlanta" :b "New Orleans" :cost 4 :color :yellow}
    {:a "Atlanta" :b "Raleigh" :cost 2 :color :gray}
    {:a "Boston" :b "Montreal" :cost 2 :color :gray}
    {:a "Boston" :b "Montreal" :cost 2 :color :gray}
    {:a "Boston" :b "New York" :cost 2 :color :red}
    {:a "Boston" :b "New York" :cost 2 :color :yellow}
    {:a "Calgary" :b "Helena" :cost 4 :color :gray}
    {:a "Calgary" :b "Seattle" :cost 4 :color :gray}
    {:a "Calgary" :b "Vancouver" :cost 3 :color :gray}
    {:a "Calgary" :b "Winnipeg" :cost 6 :color :white}
    {:a "Charleston" :b "Miami" :cost 4 :color :pink}
    {:a "Charleston" :b "Raleigh" :cost 1 :color :gray}
    {:a "Chicago" :b "Duluth" :cost 3 :color :red}
    {:a "Chicago" :b "Omaha" :cost 4 :color :blue}
    {:a "Chicago" :b "Pittsburgh" :cost 3 :color :black}
    {:a "Chicago" :b "Pittsburgh" :cost 3 :color :orange}
    {:a "Chicago" :b "Saint Louis" :cost 2 :color :green}
    {:a "Chicago" :b "Saint Louis" :cost 2 :color :white}
    {:a "Chicago" :b "Toronto" :cost 4 :color :white}
    {:a "Dallas" :b "El Paso" :cost 4 :color :red}
    {:a "Dallas" :b "Houston" :cost 1 :color :gray}
    {:a "Dallas" :b "Houston" :cost 1 :color :gray}
    {:a "Dallas" :b "Little Rock" :cost 2 :color :gray}
    {:a "Dallas" :b "Oklahoma City" :cost 2 :color :gray}
    {:a "Dallas" :b "Oklahoma City" :cost 2 :color :gray}
    {:a "Denver" :b "Helena" :cost 4 :color :green}
    {:a "Denver" :b "Kansas City" :cost 4 :color :black}
    {:a "Denver" :b "Kansas City" :cost 4 :color :orange}
    {:a "Denver" :b "Oklahoma City" :cost 4 :color :red}
    {:a "Denver" :b "Omaha" :cost 4 :color :pink}
    {:a "Denver" :b "Phoenix" :cost 4 :color :white}
    {:a "Denver" :b "Salt Lake City" :cost 3 :color :red}
    {:a "Denver" :b "Salt Lake City" :cost 3 :color :yellow}
    {:a "Denver" :b "Santa Fe" :cost 2 :color :gray}
    {:a "Duluth" :b "Helena" :cost 6 :color :orange}
    {:a "Duluth" :b "Omaha" :cost 2 :color :gray}
    {:a "Duluth" :b "Omaha" :cost 2 :color :gray}
    {:a "Duluth" :b "Sault St. Marie" :cost 3 :color :gray}
    {:a "Duluth" :b "Toronto" :cost 6 :color :pink}
    {:a "Duluth" :b "Winnipeg" :cost 4 :color :black}
    {:a "El Paso" :b "Houston" :cost 6 :color :green}
    {:a "El Paso" :b "Los Angeles" :cost 6 :color :black}
    {:a "El Paso" :b "Oklahoma City" :cost 5 :color :yellow}
    {:a "El Paso" :b "Phoenix" :cost 3 :color :gray}
    {:a "El Paso" :b "Santa Fe" :cost 2 :color :gray}
    {:a "Helena" :b "Calgary" :cost 4 :color :gray}
    {:a "Helena" :b "Omaha" :cost 5 :color :red}
    {:a "Helena" :b "Seattle" :cost 6 :color :yellow}
    {:a "Helena" :b "Winnipeg" :cost 4 :color :blue}
    {:a "Houston":b "New Orleans" :cost 2 :color :gray}
    {:a "Kansas City" :b "Oklahoma City" :cost 2 :color :gray}
    {:a "Kansas City" :b "Oklahoma City" :cost 2 :color :gray}
    {:a "Kansas City" :b "Omaha" :cost 1 :color :gray}
    {:a "Kansas City" :b "Omaha" :cost 1 :color :gray}
    {:a "Kansas City" :b "Saint Louis" :cost 2 :color :blue}
    {:a "Kansas City" :b "Saint Louis" :cost 2 :color :pink}
    {:a "Las Vegas" :b "Los Angeles" :cost 2 :color :gray}
    {:a "Las Vegas" :b "Salt Lake City" :cost 3 :color :orange}
    {:a "Little Rock" :b "Nashville" :cost 3 :color :white}
    {:a "Little Rock" :b "New Orleans" :cost 3 :color :green}
    {:a "Little Rock" :b "Oklahoma City" :cost 2 :color :gray}
    {:a "Little Rock" :b "Saint Louis" :cost 2 :color :gray}
    {:a "Los Angeles" :b "Phoenix" :cost 2 :color :black}
    {:a "Los Angeles" :b "San Francisco" :cost 3 :color :pink}
    {:a "Los Angeles" :b "San Francisco" :cost 3 :color :yellow}
    {:a "Miami" :b "New Orleans" :cost 6 :color :red}
    {:a "Montreal" :b "New York" :cost 3 :color :blue}
    {:a "Montreal" :b "Sault St. Marie" :cost 5 :color :black}
    {:a "Montreal" :b "Toronto" :cost 3 :color :gray}
    {:a "Nashville" :b "Pittsburgh" :cost 4 :color :yellow}
    {:a "Nashville" :b "Raleigh" :cost 3 :color :black}
    {:a "Nashville" :b "Saint Louis" :cost 2 :color :gray}
    {:a "New York" :b "Pittsburgh" :cost 2 :color :green}
    {:a "New York" :b "Pittsburgh" :cost 2 :color :white}
    {:a "New York" :b "Washington DC" :cost 2 :color :black}
    {:a "New York" :b "Washington DC" :cost 2 :color :orange}
    {:a "Phoenix" :b "Santa Fe" :cost 3 :color :gray}
    {:a "Pittsburgh" :b "Saint Louis" :cost 5 :color :green}
    {:a "Portland" :b "Salt Lake City" :cost 6 :color :blue}
    {:a "Portland" :b "San Francisco" :cost 5 :color :green}
    {:a "Portland" :b "San Francisco" :cost 5 :color :pink}
    {:a "Portland" :b "Seattle" :cost 1 :color :gray}
    {:a "Portland" :b "Seattle" :cost 1 :color :gray}
    {:a "Raleigh" :b "Washington DC" :cost 2 :color :gray}
    {:a "Saint Louis" :b "" :cost 2 :color :gray}
    {:a "Salt Lake City" :b "San Francisco" :cost 5 :color :orange}
    {:a "Salt Lake City" :b "San Francisco" :cost 5 :color :white}
    {:a "Sault St. Marie" :b "Toronto" :cost 2 :color :gray}
    {:a "Sault St. Marie" :b "Winnipeg" :cost 6 :color :gray}
    {:a "Seattle" :b "Vancouver" :cost 1 :color :gray}])

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
  [ks m]
  (let [f (fn [x]
            {(-> x (select-keys ks) vals set) x})]
    (into {} (map f m))))

;; Create an "index" of the edges/routes, so that looking up a particular
;; route is easy.
(def edges-index (delay (group-by-many [:a :b :color] edges)))

(defn claim!
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
  [state pname route]
  (let [player (get-in @state [:players pname])
        {:keys [color cost]} route
        {:keys [routes pieces]} player
        pieces-count (:count pieces)
        cards (:cards routes)
        player-claimed (:claimed routes)
        in-hand (count (filter #{color} cards))
        ;; This `group-by` call is going to create a hash map
        ;; like `{:red [:red :red :red]
        ;;        :yellow [:yellow :yellow]}`
        ;; This will be filtered based on whether the "cards" match the cost &
        ;; color of the route being claimed.
        color-groups (group-by identity cards)
        prismatics (:prismatic color-groups)
        valid-colors (filterv
                      (fn [[k v]]
                        (and
                         (>= (+ (count v) (count prismatics)) cost)
                         (or (= k color)
                             (= :gray color))))
                      color-groups)
        claimed-routes (get-in @state [:routes :claimed])
        score (get-in @state [:players pname :score])
        color-choice (last valid-colors)]
    (match [;; Check if `route` is claimed already.
            (= #{route} (clojure.set/intersection #{route} claimed-routes))
            (nil? color-choice)
            (>= pieces-count cost)]
           [false false true]
           (do
             ;; Move the route to the player's claimed route pile.
             (swap! state assoc-in
                    [:players pname :routes :claimed]
                    (concat player-claimed route))
             ;; Reduce the player's hand by the correct number of cards
             (swap! state assoc-in
                    [:players pname :routes :cards]
                    (let [fx (frequencies cards)
                          fy (frequencies color-choice)]
                      (apply concat
                             (for [[k v] fx]
                               (repeat (- v (get fy k 0)) k)))))
             ;; Place the used cards in the discard deck
             (swap! state assoc-in
                    [:decks :discard :cards]
                    (concat (get-in @state [:decks :discard :cards])
                            (last color-choice)))
             ;; Increment the player's score
             (swap! state assoc-in
                    [:players pname :score]
                    (+ score (get scoring cost)))
             (swap! state assoc-in
                    [:routes :claimed]
                    (clojure.set/union claimed-routes #{route}))
             {:ok player})
           [true _ _] {:error "That route is claimed"}
           [_ true _] {:error (str "Insufficient " (name color) " cards")}
           [_ _ false] {:error "Insufficient train cars"})))
