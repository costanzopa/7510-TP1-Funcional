(ns core.schema.fact-test
  (:require [clojure.test :refer :all]
   :require [core.schema.fact :refer :all]))

(def number-database ["add(zero, zero, zero).","add(zero, one, one).","subtract(X, Y, Z) :- add(Y, Z, X)."])

(def duplicate-number-database ["add(zero, zero, zero).","add(zero, one, one).","subtract(X, Y, Z) :- add(Y, Z, X).","add(zero, one, one)."])

(def uppercase-number-database ["add(zero, zero, zero).","ADD(ZERO, ONE, ONE).","subtract(X, Y, Z) :- add(Y, Z, X).","add(zero, one, one)."])

(def parent-facts [	"varon(juan).", "varon(pepe).","varon(hector).", "varon(roberto)." "varon(alejandro).", "mujer(maria)." ,"mujer(cecilia)."])

(deftest is-fact?-test
  (testing "Check if it is a fact, true answer."
    (is (= (is-fact? "add(zero, zero, zero)." ) true)))

  (testing "Check if it is a fact, false answer."
    (is (= (is-fact? "subtract(X, Y, Z) :- add(Y, Z, X)." ) false))))

(deftest filter-facts-test
  (testing "Check if normal filtering of facts is correct."
    (is (= (get-facts number-database ) ["add(zero, zero, zero).","add(zero, one, one)."])))

  (testing "Check if filtering duplicates in facts."
    (is (= (get-facts duplicate-number-database) ["add(zero, zero, zero).","add(zero, one, one)."])))

  (testing "Check if lowercase facts."
    (is (= (get-facts uppercase-number-database) ["add(zero, zero, zero).","add(zero, one, one)."]))))

(deftest get-fact-name-and-arguments-test
  (testing "Check if gets the name of the fact."
    (is (= (get-fact-name "varon(pepe).") "varon")))

    (testing "Check if gets the arguments of a fact."
      (is (= (get-fact-arguments "varon(jose, pedro).") ["jose" "pedro"]))))


(deftest is-the-query-a-fact?-test
  (testing "Check if the query is a fact stored in the database."
    (is (= (is-the-query-a-fact? parent-facts "varon(pepe).") true)))

  (testing "Checks if a query with an un-stored invoice returns false."
    (is (= (is-the-query-a-fact? parent-facts "varon(maria).") false)))
  )