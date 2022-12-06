#!/usr/bin/env bb

(require '[clojure.core :as core]
         '[clojure.string :as string]
         '[clojure.set :as set]
         '[clojure.java.io :as io]
         '[clojure.java.shell :as shell])

;; interacting with this file in a babashka repl
;; $ bb nrepl-server
;; then: (cider-connect-clj '(:host "localhost" :port 1667))

;; day 6

(let [in (slurp "inputs/6.txt")
      unique 4]                         ;  for part 2, change this to 14

  (reduce
   (fn [seen looking-at]
     (if (= unique (count (set looking-at)))
       (reduced seen)
       (inc seen)))
   unique
   (partition unique 1 in)))

;; day 5

(defn pad [n coll val]
  (take n (concat coll (repeat val))))

(defn crane-string-to-state [input]
  (let [lines (->> (string/split-lines input)
                   (map vec))
        longest-length (count (last (sort-by count lines)))
        matrix (map #(pad longest-length % \space) lines)]
    (->> matrix
         ;; transpose, then reverse, so numbers are on the right
         (apply map list)
         (map reverse)
         (map (partial apply str))
         ;; filter to column lines
         (filter (partial re-find #"^[0-9]"))
         (map string/trim)
         ;; drop the label
         (map (partial drop 1))
         (map vec)
         (into []))))

(let [in (slurp "inputs/5_example.txt")
      [crane moves] (string/split in #"\n\n")
      crane (crane-string-to-state crane)
      moves (->> moves
                 (re-seq #"[0-9]+")
                 (map read-string)
                 (partition 3)
                 (map (fn [[amount from to]]
                        [amount (dec from) (dec to)])))]

  (->> moves
       (reduce (fn [crane [amount from to]]
                 (-> crane
                     (update from (fn [stack] (vec (drop-last amount stack))))
                     (update to (fn [stack]
                                  (let [from-stack (get crane from)
                                        ;; for part 2, just remove the reverse
                                        contents (reverse (take-last amount from-stack))]
                                    (into [] (apply conj stack contents)))))))
               crane)
       (map last)
       (apply str)))

;; day 4

(let [in (slurp "inputs/4.txt")
      number-strings (-> in
                         (string/replace #"\n" ",")
                         (string/split #"[-,]"))
      pairs (->> number-strings
                 (map read-string)
                 (partition 2)
                 (map (fn [[start end]] (range start (inc end))))
                 (map set)
                 (partition 2))]

  ;; part 1
  (->> pairs
       (filter (fn [[s1 s2]]
                 (or (= s1 (set/intersection s1 s2))
                     (= s2 (set/intersection s1 s2)))))
       count)

  ;; part 2
  (->> pairs
       (remove (fn [[s1 s2]]
                 (= #{} (set/intersection s1 s2))))
       count))

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
