(ns
  ^{:doc "Defines database functions."
    :author "Pablo Costanzo"}
  core.storage.database
  (:require [clojure.string :as str]
   :require [core.schema.fact :refer :all]
   :require [core.schema.rule :refer :all]))

(defn convert-database-to-lines
  "Convert a string database to a string vector."
  [database]
  (into [] (filter not-empty (map str/trim(str/split-lines database))))
  )

(defn validate-database [database-line]
  "Validates if the line passed by parameter is a rule or a fact."
  (if (is-fact? database-line) true
      (if (is-rule? database-line) true false)))