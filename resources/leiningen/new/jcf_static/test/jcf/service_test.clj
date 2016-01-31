(ns {{ns}}.service-test
  (:require [clojure.test :refer :all]
            [{{ns}}.service :refer :all]))

(deftest t-style
  (is (= (style {:border "1px solid #123"
                 :line-height 10
                 :text-align "center"})
         "border:1px solid #123;line-height:10;text-align:center")))
