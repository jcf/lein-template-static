(ns jcf.integration-test
  (:require [clojure.java.io :as io]
            [clojure.java.shell :refer [sh]]
            [clojure.string :as str]
            [clojure.test :refer :all]
            [leiningen.new.jcf-static :refer :all]
            [leiningen.new.templates :refer [*dir*]]
            [me.raynes.fs :refer [temp-dir]]))

(defn generate-project [test-fn]
  (let [app-name "example"
        sandbox ^java.io.File (temp-dir "jcf-")]
    (binding [*dir* (str sandbox "/" app-name)]
      (println (format "Generating project in %s..." *dir*))
      (jcf-static app-name)
      (try
        (test-fn)
        (finally
          (when (.isDirectory sandbox)
            (println (format "Deleting project in %s..." *dir*))
            (.delete sandbox)))))))

(use-fixtures :once generate-project)

(deftest test-lein-test
  (let [_ (println "Running lein test. This'll take a couple of seconds...")
        {:keys [exit out err]} (sh "lein" "test" :dir *dir*)]
    (is (zero? exit)
        (format "lein test failed with status %d.\nOut:\n%s\n\nErr:\n%s\n\n"
                exit out err))))

(deftest test-lein-run
  (let [_ (println "Running lein run. This'll take a couple of seconds...")
        {:keys [exit out err]} (sh "lein" "run" :dir *dir*)]
    (is (zero? exit)
        (format "lein run failed with status %d.\nOut:\n%s\n\nErr:\n%s\n\n"
                exit out err))))
