(ns
  ^{:doc "Defines rule functions."
    :author "Pablo Costanzo"}
  core.schema.rule
  (:require [clojure.string :as str]))


(def rule-assign ":-")

(defn is-rule?
  "Determines if it's a rule."
  [line]
  (.contains line rule-assign))

(defn get-rules
  "Filter and determine the rules of the database."
  [database-lines]
  (distinct (filter is-rule? database-lines)))