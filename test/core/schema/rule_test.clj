(ns core.schema.rule-test
  (:require [clojure.test :refer :all]
   :require [core.schema.rule :refer :all]))

(def number-database ["add(zero, zero, zero).","add(zero, one, one).","subtract(X, Y, Z) :- add(Y, Z, X)."])
(def all-facts-numbers ["add(zero, zero, zero).","add(zero, one, one).", "add(zero, one, zero)."])
(def query-facts ["add(zero, zero, zero)."])
(def all-facts-parents ["varon(juan).", "varon(pepe).", "varon(hector).", "varon(roberto).", "varon(alejandro).", "mujer(maria)." "mujer(cecilia).", "padre(juan, pepe).", "padre(juan, pepa).", "padre(hector, maria).", "padre(roberto, alejandro).", "padre(roberto, cecilia)."])
(def all-rules-parent ["hijo(X, Y) :- varon(X), padre(Y, X).", "hija(X, Y) :- mujer(X), padre(Y, X)."])

(deftest is-rule-test
  (testing "Check if it is a rule, true answer."
    (is (= (is-rule? "subtract(X, Y, Z) :- add(Y, Z, X)." ) true)))

  (testing "Check if it is a fact, false answer."
    (is (= (is-rule? "add(zero, zero, zero)." ) false))))


(deftest get-rules-test
  (testing "Filter a rule, obtain the rule answer."
    (is (= (get-rules number-database ) ["subtract(X, Y, Z) :- add(Y, Z, X)."])))

  (testing "Filter a rule, obtain an empty vector."
    (is (= (get-rules all-facts-numbers ) []))))


(deftest filter-rule-right-side-test
  (testing "Gets the definition of the rules, one fact."
    (is (= (get-rule-right-side "subtract(X, Y, Z) :- add(Y, Z, X)." ) ["add(Y, Z, X)."])))

  (testing "Gets the definition of the rules, two facts."
    (is (= (get-rule-right-side "hijo(X, Y) :- varon(X), padre(Y, X)." ) ["varon(X)." "padre(Y, X)."])))
  )

(deftest replace-all-arguments-test
  (testing "Replaces arguments, passed from a map."
    (is (= (replace-all-arguments "add(Y, Z, X)." {"X" "zero" "Y" "zero" "Z" "one"}) "add(zero, one, zero)."))))


(deftest evaluate-all-facts-test
  (testing "Check if finds the query in the vector of facts, true answer."
    (is (= (evaluate-all-facts all-facts-numbers query-facts) true)))
  (testing "Check if finds the query in the vector of facts, false answer."
    (is (= (evaluate-all-facts all-facts-parents query-facts) false))))


(deftest is-the-query-a-rule?-test
  (testing "Checks is-current-rule? function."
    (is (= (is-current-rule? "subtract(X, Y, Z) :- add(Y, Z, X)." "subtract(zero, zero, one).") true)))
  (testing "Checks is-it-a-correct-rule? function."
    (is (= (is-it-a-correct-rule? "subtract(X, Y, Z) :- add(Y, Z, X)." "subtract(zero, zero, one)." all-facts-numbers) true)))

  (testing "Checks is-the-query-a-rule? function."
    (is (= (is-the-query-a-rule?  all-rules-parent all-facts-parents "hijo(pepe, juan)." ) true))))
