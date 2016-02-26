(ns {{ns}}.styles
  (:require [garden.core :as garden]
            [garden.compiler :as compiler]
            [garden.units :refer [em px]]
            [schema.core :as s]
            [clojure.string :as str]))

(s/defn ^:private quote-string :- s/Str
  [s :- s/Any]
  (if (and (string? s) (str/includes? s " "))
    (format "\"%s\"" s)
    s))

(def ^:private join-strings
  (comp (map compiler/render-css) (map quote-string)))

(s/defn csv :- s/Str
  [xs :- [s/Any]]
  (str/join "," (into [] join-strings xs)))

(s/defn ssv :- s/Str
  [xs :- [s/Any]]
  (str/join " " (into [] join-strings xs)))

(def ^:private default-font-family
  (csv ["system"
        "-apple-system"
        "BlinkMacSystemFont"
        "Helvetica Neue"
        "sans-serif"]))

(defn- rules
  []
  [[:body
    {:background-color "white"
     :color "#444"
     :font-family default-font-family
     :font-size (px 16)
     :line-height (em 1.4)
     :margin 0
     :padding 0
     :text-rendering "optimizeLegibility"}]
   [:.container
    {:margin (ssv [0 "auto"])
     :max-width (px 500)
     :padding (ssv [0 (px 20)])}]])

(s/defn css :- s/Str
  []
  (garden/css (rules)))
