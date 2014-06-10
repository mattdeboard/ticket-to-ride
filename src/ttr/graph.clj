(ns ^{:doc "This namespace is concerned with graph operations performed against
the graph representation of the Ticket To Ride board. If you are not
running Neo4j, the graph database stuff obviously will not work.

Additionally, neo4j really won't be necessary for playing the game itself,
but it's being used here frankly because I think there are some cool
opportunities for game analysis. That is, stuff that has nothing to do with
playing the game itself."}
  ttr.graph
  (:require [ttr.board :refer [edges vertices]]
            [clojurewerkz.neocons.rest :as nr]
            [clojurewerkz.neocons.rest.nodes :as nn]
            [clojurewerkz.neocons.rest.labels :as nl]
            [clojurewerkz.neocons.rest.relationships :as nrl]
            [clojurewerkz.neocons.rest.cypher :as cy]))

(def neo4j-url "http://localhost:7474/db/data")

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
