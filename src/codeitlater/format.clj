(ns codeitlater.format
  (:require [clojure.string :as str])
  (:require [clojure.java.io :as io])
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

(defn format-keyword-content [tuples keyword]
  "cut content those including keyword:
return ((linenum [keyword content])..)
"
  (let [filter-content (filter #(-> (str "(?<=" keyword ":" ").*$")
                                    (re-pattern)
                                    (re-find (second %)))
                               tuples)]
    ;;(print filter-content)
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
        ;;(println)
        (recur (rest listset))
        ))))

(defn write-keyword-sentence [ls keyword path]
  "write keyword content in level"
 (with-open [wrtr (io/writer path)]
  (loop [listset ls]
    (if (not (empty? listset))
      (let [thisls (first listset)
            [filepath & tuples] thisls]
        ;(.write wrtr tuples)
        (.write wrtr (str "*" filepath "\n"
                          (format-keyword-content tuples keyword)))
        (recur (rest listset)))))))


(defn write-to-file [path]
  (with-open [wrtr (io/writer path)]
    (.write wrtr "test")
    (.write wrtr "test1")))
