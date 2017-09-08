(ns
  ^{:doc "Defines facts functions."
    :author "Pablo Costanzo"}
  core.schema.fact
  (:require [clojure.string :as str]))

(def fact-pattern #"([^,]*)\(([^\(\)]*)\)\.$")

(defn is-fact?
  "Determines if it's a fact."
  [line]
  (cond
    (boolean (re-matches fact-pattern line)) true
    :else false))

(defn get-facts
  "Filter and determine the facts of the database."
  [database-lines]
  (distinct (filter is-fact? (map str/lower-case database-lines))))

(defn get-fact-name
  "Gets the name of the fact."
  [fact]
  (str/trim (subs fact 0 (str/index-of fact "("))))

(defn get-fact-arguments
  "Gets the arguments of a fact in a vector."
  [fact]
  (map str/trim (str/split(subs fact (+ (str/index-of fact "(") 1) (str/index-of fact ")")) #",")))


(defn is-the-query-a-fact?
  "Determine if the query is in the vector of facts."
  [facts query]
  (cond (not (nil? (some #{query} facts))) true
        :else false))