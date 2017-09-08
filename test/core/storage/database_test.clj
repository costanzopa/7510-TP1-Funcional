(ns core.storage.database-test
  (:require [clojure.test :refer :all]
            [core.storage.database :refer :all]))

(def complete-database "
	varon(juan).
	varon(pedro).
	hija(X, Y) :- mujer(X), padre(Y, X).
	")

(def incomplete-database "
	varon(juan).
	varon
")

(def empty-database "")

(deftest convert-to-lines-test
  (testing "Test if converts a complete string database to a string vector."
    (is (= (convert-database-to-lines complete-database) ["varon(juan).","varon(pedro).","hija(X, Y) :- mujer(X), padre(Y, X)."])))

  (testing "Test if converts a incomplete string database to a string vector."
    (is (= (convert-database-to-lines incomplete-database) ["varon(juan).","varon"])))

  (testing "Test if converts an empty string database to a string vector."
    (is (= (convert-database-to-lines empty-database) []))))


(deftest validate-database-test
  (testing "Check if a valid fact composes a valid database."
    (is (= (validate-database "varon(juan).") true)))

  (testing "Check if an invalid fact composes a valid database"
    (is (= (validate-database "varon(") false)))

  (testing "Check if a valid rule composes a valid database"
    (is (= (validate-database "hija(X, Y) :- mujer(X), padre(Y, X).") true)))

  (testing "Check if an invalid rule composes a valid database."
    (is (= (validate-database "hija(X, Y) mujer(X), padre(Y, X).") false))))

(deftest string-database-validate-test
  (testing "Test if a string database has a-invalid-fact."
    (is (= (some false? (map validate-database ["varon(juan).","varon("]))  true)))

  (testing "Test if a string database has all the facts valid."
    (is (= (some false? (map validate-database ["varon(juan).","varon(pedro)."]))  nil))))
