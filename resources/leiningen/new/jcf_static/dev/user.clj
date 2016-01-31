(ns user
  (:require [com.stuartsierra.component :as component]
            [reloaded.repl :refer [go init reset start stop system]]
            [{{ns}}.service :as service]))

(reloaded.repl/set-init! #(service/new-system {}))
