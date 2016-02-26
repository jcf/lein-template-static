(ns jcf.template-test
  (:require [clojure.test :refer :all]
            [leiningen.new.jcf-static :refer :all]
            [schema.test :refer [validate-schemas]]))

(use-fixtures :once validate-schemas)

(def ^:private expected-manifest
  [".gitignore"
   "dev/user.clj"
   "project.clj"
   "resources/public/css/normalize.css"
   "src/{{path}}/service.clj"
   "src/{{path}}/styles.clj"
   "test/{{path}}/service_test.clj"
   "test/{{path}}/styles_test.clj"])

(def manifest
  (render-files (get-manifest clojurish-templates)
                {:name "example/app"
                 :ns "example.app"
                 :path "example/app"
                 :project-name "app"
                 :hyphenated-name "example-app"}))

(deftest test-name->data
  (are [in out] (= (name->data in) out)
    "foo"
    {:hyphenated-name "foo"
     :name "foo"
     :ns "foo"
     :path "foo"
     :project-name "foo"}

    "foo.app"
    {:hyphenated-name "foo.app"
     :name "foo.app"
     :ns "foo.app"
     :path "foo/app"
     :project-name "foo.app"}

    "foo/app"
    {:hyphenated-name "foo-app"
     :name "foo/app"
     :ns "foo.app"
     :path "foo/app"
     :project-name "app"}

    "foo/bar/app"
    {:hyphenated-name "foo-bar-app"
     :name "foo/bar/app"
     :ns "foo.bar.app"
     :path "foo/bar/app"
     :project-name "app"}))

(deftest test-render-files
  (is (= (-> manifest keys sort) expected-manifest)))

(deftest test-project-definition
  (let [[_ named version & kvs] (-> manifest (get "project.clj") read-string)
        props (apply hash-map kvs)]
    (is (= named 'example/app))
    (is (= version "0.1.0-SNAPSHOT"))
    (is (= (:main props) 'example.app.service))
    (is (= (get-in props [:repl-options :init-ns]) 'user))
    (is (nil? (:uberjar-name props)))
    (is (= (:dependencies props)
           '[[com.stuartsierra/component "0.3.1"]
             [garden "1.3.1"]
             [hiccup "1.0.5"]
             [optimus "0.18.4"]
             [org.clojure/clojure "1.8.0"]
             [prismatic/schema "1.0.4"]
             [ring/ring-core "1.4.0"]
             [ring/ring-jetty-adapter "1.4.0"]
             [stasis "2.3.0"]]))
    (is (= (get-in props [:profiles :dev :dependencies])
           '[[enlive "1.1.6"]
             [org.clojure/tools.namespace "0.2.10"]
             [reloaded.repl "0.2.1"]
             [ring/ring-mock "0.3.0"]]))))
