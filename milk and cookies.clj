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

(let [in (slurp "inputs/1.txt")]
  (->> (string/split in #"\n\n")
       (map (fn [parts]
              (map read-string (string/split parts #"\n"))))
       (map (partial apply +))
       (sort >)
       ;; (first)                       ; part 1
       (take 3)
       (apply +)))                      ; part 2
