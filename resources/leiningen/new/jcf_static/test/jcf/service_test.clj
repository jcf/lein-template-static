(ns {{ns}}.service-test
  (:require [clojure.test :refer :all]
            [{{ns}}.service :refer :all]
            [net.cgrand.enlive-html :as html]
            [ring.mock.request :as mock]
            [schema.test :refer [validate-schemas]]
            [clojure.string :as str]))

(use-fixtures :once validate-schemas)

(deftest t-home-page
  (let [{:keys [status body]} (app (mock/request :get "/"))
        doc (html/html-snippet body)]
    (is (= 200 status))
    (is (= ["FIXME"] (-> doc (html/select [:h1]) first :content)))))
