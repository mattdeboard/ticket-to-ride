(ns ^{:doc "This namespace is concerned entirely with the board. If you are not
running Neo4j, the graph database stuff obviously will not work.

Additionally, neo4j really won't be necessary for playing the game itself,
but it's being used here frankly because I think there are some cool
opportunities for game analysis. That is, stuff that has nothing to do with
playing the game itself."}
  ttr.board
  (:require [clojurewerkz.neocons.rest :as nr]
            [clojurewerkz.neocons.rest.nodes :as nn]
            [clojurewerkz.neocons.rest.labels :as nl]
            [clojurewerkz.neocons.rest.relationships :as nrl]
            [clojurewerkz.neocons.rest.cypher :as cy]))

(def neo4j-url "http://localhost:7474/db/data")
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
   {:a "Atlanta" :b "Charleston" :cost 2 :color "gray"}
   {:a "Atlanta" :b "Miami" :cost 5 :color "blue"}
   {:a "Atlanta" :b "Nashville" :cost 1 :color "gray"}
   {:a "Atlanta" :b "New Orleans" :cost 4 :color "orange"}
   {:a "Atlanta" :b "New Orleans" :cost 4 :color "yellow"}
   {:a "Atlanta" :b "Raleigh" :cost 2 :color "gray"}
   {:a "Boston" :b "Montreal" :cost 2 :color "gray"}
   {:a "Boston" :b "Montreal" :cost 2 :color "gray"}
   {:a "Boston" :b "New York" :cost 2 :color "red"}
   {:a "Boston" :b "New York" :cost 2 :color "yellow"}
   {:a "Calgary" :b "Helena" :cost 4 :color "gray"}
   {:a "Calgary" :b "Seattle" :cost 4 :color "gray"}
   {:a "Calgary" :b "Vancouver" :cost 3 :color "gray"}
   {:a "Calgary" :b "Winnipeg" :cost 6 :color "white"}
   {:a "Charleston" :b "Miami" :cost 4 :color "pink"}
   {:a "Charleston" :b "Raleigh" :cost 1 :color "gray"}
   {:a "Chicago" :b "Duluth" :cost 3 :color "red"}
   {:a "Chicago" :b "Omaha" :cost 4 :color "blue"}
   {:a "Chicago" :b "Pittsburgh" :cost 3 :color "black"}
   {:a "Chicago" :b "Pittsburgh" :cost 3 :color "orange"}
   {:a "Chicago" :b "Saint Louis" :cost 2 :color "green"}
   {:a "Chicago" :b "Saint Louis" :cost 2 :color "white"}
   {:a "Chicago" :b "Toronto" :cost 4 :color "white"}
   {:a "Dallas" :b "El Paso" :cost 4 :color "red"}
   {:a "Dallas" :b "Houston" :cost 1 :color "gray"}
   {:a "Dallas" :b "Houston" :cost 1 :color "gray"}
   {:a "Dallas" :b "Little Rock" :cost 2 :color "gray"}
   {:a "Dallas" :b "Oklahoma City" :cost 2 :color "gray"}
   {:a "Dallas" :b "Oklahoma City" :cost 2 :color "gray"}
   {:a "Denver" :b "Helena" :cost 4 :color "green"}
   {:a "Denver" :b "Kansas City" :cost 4 :color "black"}
   {:a "Denver" :b "Kansas City" :cost 4 :color "orange"}
   {:a "Denver" :b "Oklahoma City" :cost 4 :color "red"}
   {:a "Denver" :b "Omaha" :cost 4 :color "pink"}
   {:a "Denver" :b "Phoenix" :cost 4 :color "white"}
   {:a "Denver" :b "Salt Lake City" :cost 3 :color "red"}
   {:a "Denver" :b "Salt Lake City" :cost 3 :color "yellow"}
   {:a "Denver" :b "Santa Fe" :cost 2 :color "gray"}
   {:a "Duluth" :b "Helena" :cost 6 :color "orange"}
   {:a "Duluth" :b "Omaha" :cost 2 :color "gray"}
   {:a "Duluth" :b "Omaha" :cost 2 :color "gray"}
   {:a "Duluth" :b "Sault St. Marie" :cost 3 :color "gray"}
   {:a "Duluth" :b "Toronto" :cost 6 :color "pink"}
   {:a "Duluth" :b "Winnipeg" :cost 4 :color "black"}
   {:a "El Paso" :b "Houston" :cost 6 :color "green"}
   {:a "El Paso" :b "Los Angeles" :cost 6 :color "black"}
   {:a "El Paso" :b "Oklahoma City" :cost 5 :color "yellow"}
   {:a "El Paso" :b "Phoenix" :cost 3 :color "gray"}
   {:a "El Paso" :b "Santa Fe" :cost 2 :color "gray"}
   {:a "Helena" :b "Calgary" :cost 4 :color "gray"}
   {:a "Helena" :b "Omaha" :cost 5 :color "red"}
   {:a "Helena" :b "Seattle" :cost 6 :color "yellow"}
   {:a "Helena" :b "Winnipeg" :cost 4 :color "blue"}
   {:a "Houston":b "New Orleans" :cost 2 :color "gray"}
   {:a "Kansas City" :b "Oklahoma City" :cost 2 :color "gray"}
   {:a "Kansas City" :b "Oklahoma City" :cost 2 :color "gray"}
   {:a "Kansas City" :b "Omaha" :cost 1 :color "gray"}
   {:a "Kansas City" :b "Omaha" :cost 1 :color "gray"}
   {:a "Kansas City" :b "Saint Louis" :cost 2 :color "blue"}
   {:a "Kansas City" :b "Saint Louis" :cost 2 :color "pink"}
   {:a "Las Vegas" :b "Los Angeles" :cost 2 :color "gray"}
   {:a "Las Vegas" :b "Salt Lake City" :cost 3 :color "orange"}
   {:a "Little Rock" :b "Nashville" :cost 3 :color "white"}
   {:a "Little Rock" :b "New Orleans" :cost 3 :color "green"}
   {:a "Little Rock" :b "Oklahoma City" :cost 2 :color "gray"}
   {:a "Little Rock" :b "Saint Louis" :cost 2 :color "gray"}
   {:a "Los Angeles" :b "Phoenix" :cost 2 :color "black"}
   {:a "Los Angeles" :b "San Francisco" :cost 3 :color "pink"}
   {:a "Los Angeles" :b "San Francisco" :cost 3 :color "yellow"}
   {:a "Miami" :b "New Orleans" :cost 6 :color "red"}
   {:a "Montreal" :b "New York" :cost 3 :color "blue"}
   {:a "Montreal" :b "Sault St. Marie" :cost 5 :color "black"}
   {:a "Montreal" :b "Toronto" :cost 3 :color "gray"}
   {:a "Nashville" :b "Pittsburgh" :cost 4 :color "yellow"}
   {:a "Nashville" :b "Raleigh" :cost 3 :color "black"}
   {:a "Nashville" :b "Saint Louis" :cost 2 :color "gray"}
   {:a "New York" :b "Pittsburgh" :cost 2 :color "green"}
   {:a "New York" :b "Pittsburgh" :cost 2 :color "white"}
   {:a "New York" :b "Washington DC" :cost 2 :color "black"}
   {:a "New York" :b "Washington DC" :cost 2 :color "orange"}
   {:a "Phoenix" :b "Santa Fe" :cost 3 :color "gray"}
   {:a "Pittsburgh" :b "Saint Louis" :cost 5 :color "green"}
   {:a "Portland" :b "Salt Lake City" :cost 6 :color "blue"}
   {:a "Portland" :b "San Francisco" :cost 5 :color "green"}
   {:a "Portland" :b "San Francisco" :cost 5 :color "pink"}
   {:a "Portland" :b "Seattle" :cost 1 :color "gray"}
   {:a "Portland" :b "Seattle" :cost 1 :color "gray"}
   {:a "Raleigh" :b "Washington DC" :cost 2 :color "gray"}
   {:a "Saint Louis" :b "" :cost 2 :color "gray"}
   {:a "Salt Lake City" :b "San Francisco" :cost 5 :color "orange"}
   {:a "Salt Lake City" :b "San Francisco" :cost 5 :color "white"}
   {:a "Sault St. Marie" :b "Toronto" :cost 2 :color "gray"}
   {:a "Sault St. Marie" :b "Winnipeg" :cost 6 :color "gray"}
   {:a "Seattle" :b "Vancouver" :cost 1 :color "gray"}
   ])

(defn create-idx-city-name
  "Create an index on city name in neo4j."
  []
  (let [conn (nr/connect neo4j-url)]
    (cy/query conn "create constraint on (n:City) assert n.name is unique")))

(defn create-node
  "Creates a node (aka a city or stop in TTR).

The only metadata for a node is the name of the city. All other interesting
data are attached to the edge/route."
  [vertex]
  (let [conn (nr/connect neo4j-url)
        node (nn/create conn vertex)]
    (nl/add conn node :City)))

(defn create-all-nodes []
  (map vertices create-node))

(defn create-edge
  "Create an edge (aka a route in Ticket to Ride terms) with appropriate
metadata.

Metadata for routes is composed of color & cost. The latter is how many
train cards of a particular color is will take to claim the route."
  [edge]
  (create-idx-city-name)
  (let [conn (nr/connect neo4j-url)
        {:keys [a b color cost]} edge]
    (cy/query conn
              "
match (c1:City), (c2:City)
where c1.name = {name1} and c2.name = {name2}
create (c1)-[r1:Route {color: {color}, cost: {cost}}]->(c2)
return r1"
              {:name1 a :name2 b :color color :cost cost})))

(defn create-all-edges []
  (map create-edge edges))

(defn fetch-node [name]
  (let [conn (nr/connect neo4j-url)]
    (cy/tquery conn
               "
match (city:City {name: {name}})-[:route]->end
return end limit 1"
               {:name name})))
