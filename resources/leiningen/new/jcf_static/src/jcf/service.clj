(ns {{ns}}.service
  (:gen-class)
  (:require [{{ns}}.styles :as styles]
            [clojure.string :as str]
            [com.stuartsierra.component :as component]
            [hiccup.page :as page]
            [optimus export html
             [assets :as assets]
             [link :as link]
             [optimizations :as optimizations]
             [prime :as optimus]
             [strategies :refer [serve-live-assets]]]
            [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware
             [content-type :refer [wrap-content-type]]
             [reload :refer [wrap-reload]]]
            [schema.core :as s]
            [stasis.core :as stasis])
  (:import org.eclipse.jetty.server.Server))

;; -----------------------------------------------------------------------------
;; Templates

(defn render-page
  "Render content into an HTML5 string"
  [context & main]
  (page/html5
   [:head
    [:title "FIXME"]
    (optimus.html/link-to-css-bundles context ["app.css"])]
   [:body
    [:header [:h1 "FIXME"]]
    [:main.container main]]))

;; -----------------------------------------------------------------------------
;; Manifest

(defn get-assets
  "https://github.com/magnars/optimus"
  []
  (concat
   (assets/load-bundle "public" "app.css" ["/css/normalize.css"])
   [{:path "/css/main.css"
     :contents (styles/css)
     :bundle "app.css"}]))

(defn get-pages
  "Returns paths mapped to their content."
  []
  {"/index.html" #(render-page % [:h1 "FIXME"])})

;; -----------------------------------------------------------------------------
;; Components

(def app
  "Ring handler that serves pages and static assets."
  (-> (stasis/serve-pages get-pages)
      (optimus/wrap get-assets optimizations/all serve-live-assets)
      wrap-content-type))

(defrecord HTTP [port]
  component/Lifecycle
  (start [c]
    (assoc c :server (run-jetty (wrap-reload #'app)
                                {:host "0.0.0.0" :port port :join? false})))
  (stop [c]
    (when-let [^Server server (:server c)] (.stop server))
    (assoc c :server nil)))

(defn new-system
  [config]
  (let [config (merge {:port 4000} config)]
    (component/system-map
     :http (map->HTTP config))))

;; -----------------------------------------------------------------------------
;; CLI

(def ^:private target-dir
  "Path to export generated site to"
  "target/public")

(defn -main
  "Main entry point. Generates the site to hardcoded `target-dir`."
  []
  (let [assets (optimizations/all (get-assets) {})
        pages (get-pages)]
    (println (format "Generating %s page(s) to %s..." (count pages) target-dir))
    (stasis/empty-directory! target-dir)
    (optimus.export/save-assets assets target-dir)
    (stasis/export-pages pages target-dir {:optimus-assets assets})))
