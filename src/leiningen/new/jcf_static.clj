(ns leiningen.new.jcf-static
  (:require [clojure.string :as str]
            [leiningen.core.main :as main]
            [leiningen.new.templates :as tmpl]
            [schema.core :as s]))

(defn- hyphenated-name
  [s]
  (str/replace s #"/" "-"))

(def ^:private render
  (tmpl/renderer "jcf_static"))

(def clojurish-templates
  "All templates, with the exception of `gitignore` which needs to be 'hidden'
  from git."
  ["dev/user.clj"
   "project.clj"
   "resources/public/css/main.css"
   "resources/public/css/normalize.css"
   "src/jcf/service.clj"
   "test/jcf/service_test.clj"])

(def ^:private Templates
  {s/Str s/Str})

(s/defn ^:private expand-paths :- Templates
  "Take a list of template paths, and expand them into a map of destination
  mapped to template path.

  Any instance of `jcf` in the template path will be replaced with
  `{{path}}`."
  [paths :- [s/Str]]
  (->> paths
       (map (juxt #(.replace ^String % "jcf" "{{path}}") identity))
       (into {})))

(s/defn get-manifest :- Templates
  [paths :- [s/Str]]
  (assoc (expand-paths paths) ".gitignore" "gitignore"))

(s/defn render-files :- Templates
  "Given a list of destinations and template paths, maps over the template paths
  and renders each file using the `jcf` renderer."
  [files :- Templates data :- {s/Keyword s/Str}]
  (reduce-kv #(assoc %1 %2 (render %3 data)) {} files))

(defn name->data
  [named]
  {:hyphenated-name (hyphenated-name named)
   :name named
   :ns (tmpl/sanitize-ns named)
   :path (tmpl/name-to-path named)
   :project-name (tmpl/project-name named)})

(s/defn jcf-static
  [named :- s/Str]
  (let [data (name->data named)
        files (get-manifest clojurish-templates)]
    (main/info (format "Generating %d files..." (count files)))
    (apply tmpl/->files data (render-files files data))))
