(defproject jcf-static/lein-template "0.5.0-SNAPSHOT"
  :description "A Leiningen template I use for quickly creating a static site."
  :url "https://github.com/jcf/lein-template-static"
  :license {:name "The MIT License"
            :url "http://opensource.org/licenses/MIT"}
  :deploy-repositories [["releases" :clojars]]
  :eval-in-leiningen true
  :dependencies [[prismatic/schema "0.4.3"]]
  :profiles {:dev {:dependencies [[leiningen "2.5.1"]
                                  [me.raynes/fs "1.4.6"]
                                  [org.clojure/clojure "1.8.0"]]}})
