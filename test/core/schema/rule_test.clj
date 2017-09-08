(ns core.schema.rule-test
  (:require [clojure.test :refer :all]
   :require [core.schema.rule :refer :all]))



(deftest is-rule-test
  (testing "Check if it is a rule, true answer."
    (is (= (is-rule? "subtract(X, Y, Z) :- add(Y, Z, X)." ) true)))

  (testing "Check if it is a fact, false answer."
    (is (= (is-rule? "add(zero, zero, zero)." ) false))))

