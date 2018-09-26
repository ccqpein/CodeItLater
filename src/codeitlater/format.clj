(ns codeitlater.format
  (:require [clojure.string :as str])
  )

(defn printline [filepath tuples]
  "tuple => ((linenum string)...)"
  (do
    (printf "|-- %s\n" filepath)
    (doall (map #(printf "  |-- %s\n" %) tuples))
    (println)))

(defn print-with-keyword [filepath tuples keyword]
  (let [filter-content (filter #(-> (str "(?<=" keyword ":" ").*$")
                                    (re-pattern)
                                    (re-find (second %)))
                               tuples)]
    (if (not (empty? filter-content))
      (printline filepath filter-content)
      )))

(defn format-keyword-content [filepath tuples keyword]
  "cut content those including keyword"
  (let [filter-content (filter #(-> (str "(?<=" keyword ":" ").*$")
                                    (re-pattern)
                                    (re-find (second %)))
                               tuples)]
    (if (not (empty? filter-content))
      (doall (map #(list (first %) (str/split (second %) #": ")) filter-content))
      )))

(defn list2tree [ls keyword]
  "make list to tree"
  ;:= print for test
  ;;(println ls)
  (loop [listset ls]
    (if (not (empty? listset))
          (let [thisls (first listset)
                [filepath & tuples] thisls]
            (if keyword
              (print-with-keyword filepath tuples keyword)
              (printline filepath tuples)
              )
            ;(println)
            (recur (rest listset))
            ))))

;;;:= TODO: need to clean ':' after keyword
(defn write-keyword-sentence [level content]
  "write keyword content in level"
  (let [this_level_mark (reduce str
                                (take (inc level)
                                      (repeat "*")))]
    ;;(print this_level_mark)
    (doall (map #(str this_level_mark
                      " "
                      (second %))
                content))))
