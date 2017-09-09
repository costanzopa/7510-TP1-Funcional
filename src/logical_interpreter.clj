(ns
  ^{:doc "Logical Interpreter."
    :author "Pablo Costanzo"}
  logical-interpreter
  (:require [core.schema.rule :refer :all]
   :require [core.schema.fact :refer :all]
   :require [core.storage.database :refer :all]
   :require [clojure.string :as str]))

(defn execute-query
  "After validating the database,
   execute the logical engine and determine if the query is a fact or a rule."
  [database-lines query]
  (let [facts (get-facts database-lines)
        rules (get-rules database-lines)]
    (cond
      (or (is-the-query-a-fact? facts query)
          (is-the-query-a-rule? rules facts query)
          ) true
      :else false
      )
    )
  )

(defn evaluate-query
  "Returns true if the rules and facts in database imply query, false if not. If
  either input can't be parsed, returns nil."
  [database query]
  (let [database-lines (convert-database-to-lines database)
        fact-query  (str (str/lower-case query) ".")]
    (cond (some false? (map validate-database (conj database-lines fact-query))) nil
          :else (execute-query database-lines fact-query)
          )
    )
  )