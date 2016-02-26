(ns {{ns}}.styles-test
  (:require [{{ns}}.styles :refer :all]
            [clojure.test :refer :all]
            [schema.test :refer [validate-schemas]]))

(use-fixtures :once validate-schemas)

(deftest t-csv
  (is (= (csv []) ""))
  (is (= (csv ["a" "b c"]) "a,\"b c\"")))

(deftest t-ssv
  (is (= (ssv []) ""))
  (is (= (ssv ["a" "b c"]) "a \"b c\"")))

(deftest t-css
  (is (string? (css))))
