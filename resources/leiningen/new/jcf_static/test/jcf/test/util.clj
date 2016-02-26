(ns {{ns}}.test.util
  (:require [com.stuartsierra.component :as component]))

(defn with-component
  "Start and stop `component` around the given function `f`. Yields the running
  component to the `f`."
  [component f]
  (let [running (component/start component)]
    (try
      (f running)
      (finally
        (component/stop running)))))
