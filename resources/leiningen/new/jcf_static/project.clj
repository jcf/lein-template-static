(defproject {{name}} "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "https://example.com/FIXME"
  :dependencies [[com.stuartsierra/component "0.3.1"]
                 [garden "1.3.1"]
                 [hiccup "1.0.5"]
                 [optimus "0.18.4"]
                 [org.clojure/clojure "1.8.0"]
                 [prismatic/schema "1.0.4"]
                 [ring/ring-core "1.4.0"]
                 [ring/ring-devel "1.4.0"]
                 [ring/ring-jetty-adapter "1.4.0"]
                 [stasis "2.3.0"]]
  :main {{ns}}.service
  :min-lein-version "2.5.0"
  :repl-options {:init-ns user}
  :profiles
  {:dev {:dependencies [[enlive "1.1.6"]
                        [org.clojure/tools.namespace "0.2.10"]
                        [reloaded.repl "0.2.1"]
                        [ring/ring-mock "0.3.0"]]
         :source-paths ["dev"]}})
