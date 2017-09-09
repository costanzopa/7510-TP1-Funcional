(ns
  ^{:doc "Defines rule functions."
    :author "Pablo Costanzo"}
  core.schema.rule
  (:require [clojure.string :as str]
   :require [core.schema.fact :refer :all]))


(def rule-assign ":-")

(defn is-rule?
  "Determines if it's a rule."
  [line]
  (.contains line rule-assign))

(defn get-rules
  "Filter and determine the rules of the database."
  [database-lines]
  (distinct (filter is-rule? database-lines)))


(defn get-rule-right-side
  "Gets the definition of the rules.
   Ex: hijo(X, Y) :- varon(X), padre(Y, X).
   ['varon(X).' 'padre(Y, X).']"
  [rule]
  (let [right-side (subs rule (+ (str/index-of rule "-" ) 2))]
    (str/split (str/replace right-side #"\), " ").:") #":")))


(defn replace-all-arguments
  "Replaces arguments, passed from a map."
  [fact parameters-to-replace]
  (str/replace fact (re-pattern (str/join "|" (keys parameters-to-replace))) parameters-to-replace))


(defn evaluate-all-facts
  "Evaluates that all the facts obtained from the rule,
   are facts obtained from the database."
  [facts all-facts-of-query]
  (cond (some false? (map #(.contains facts %) all-facts-of-query)) false
        :else true))


(defn is-it-a-correct-rule?
  "Determine if the rule is a composition of valid fatcs."
  [rule query facts]
  (let [rule-arguments (get-fact-arguments rule)
        query-arguments (get-fact-arguments query)
        parameters-to-replace (zipmap rule-arguments query-arguments)
        rule-right-side (get-rule-right-side rule)]
    (let [all-facts-of-query (map (fn [fact] (replace-all-arguments fact parameters-to-replace)) rule-right-side)]
      (evaluate-all-facts facts all-facts-of-query))))

(defn is-current-rule?
  "Determines if a rule is in the database."
  [rule query]
  (let [rule-name (get-fact-name rule)
        query-name (get-fact-name query)]
    (if (= rule-name query-name) true
                                 false)))

(defn is-the-query-a-rule?
  "Determines whether the query meets all the conditions to be a rule."
  [rules facts query]
  (let [current-rule (first (filter #(is-current-rule? % query) rules)) ]
    (cond (not (nil? current-rule)) (is-it-a-correct-rule? current-rule query facts)
          :else false)))