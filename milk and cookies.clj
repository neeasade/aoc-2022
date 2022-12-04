#!/usr/bin/env bb

(require '[clojure.core :as core]
         '[clojure.string :as string]
         '[clojure.set :as set]
         '[clojure.java.io :as io]
         '[clojure.java.shell :as shell])

;; interacting with this file in a babashka repl
;; $ bb nrepl-server
;; then: (cider-connect-clj '(:host "localhost" :port 1667))

;; day 3

(defn char-to-priority [c]
  (mod (- (inc (int c))
          (int \a))
       58))

(let [sacks (string/split-lines (slurp "inputs/3.txt"))]
  ;; part 1
  (->> sacks
       (map (fn [sack] (split-at (/ (count sack) 2) sack)))
       (map (fn [compartments] (apply set/intersection (map set compartments))))
       ;; there's always only one intersecting type?
       (map first)
       (map char-to-priority)
       (apply +))

  ;; part 2
  (->> sacks
       (map seq)
       (partition 3)
       (map (fn [team-sacks] (apply set/intersection (map set team-sacks))))
       (map first)
       (map char-to-priority)
       (apply +)))

;; day 2 (fixme)

(let [in (-> (slurp "inputs/2.txt")
             (string/replace #"(X|A)" "1")
             (string/replace #"(Y|B)" "2")
             (string/replace #"(Z|C)" "3"))]
  (->> in
       (re-seq #"[1-3]")
       (map read-string)
       (partition 2)
       (map (fn [[them us]]
              (+ us (cond (< them us) 6
                          (= them us) 3
                          (> them us) 0))))
       ;; (apply +)
       ))

;; day 1

(let [in (slurp "inputs/1.txt")]
  (->> (string/split in #"\n\n")
       (map (fn [parts]
              (map read-string (string/split-lines parts #"\n"))))
       (map (partial apply +))
       (sort >)
       ;; (first)                       ; part 1
       (take 3)
       (apply +)))                      ; part 2
