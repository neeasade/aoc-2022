#!/usr/bin/env bb

(require '[clojure.core :as core]
         '[clojure.string :as string]
         '[clojure.set :as set]
         '[clojure.java.io :as io]
         '[clojure.java.shell :as shell])

;; interacting with this file in a babashka repl
;; $ bb nrepl-server
;; then: (cider-connect-clj '(:host "localhost" :port 1667))

;; day 1
