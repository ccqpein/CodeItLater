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


(defn check-keyword-content [tuples keyword]
  "cut content those including keyword:
return ((linenum \"keyword content\")..)
"
  (let [filter-content (filter #(-> (str "(?<=" keyword ":" ").*$")
                                    (re-pattern)
                                    (re-find (second %)))
                               tuples)]
    ;;(print filter-content)
    (if (not (empty? filter-content))
      (map #(list (first %) (str/split (second %) #": ")) filter-content)
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


(defn format-content [line]
  "line struct is '(1 [keyword content])"
  (format "%s (in line %d)\n" (second (second line)) (first line))
  )


(defn write-keyword-sentence [ls keyword path]
  "write keyword content in level"
  (let [realpath (if (.isDirectory (io/file path))
                   (do (printf "%s is directory cannot write org file, write in ./project.org"
                               path)
                       "./project.org")
                   path)]
    (with-open [wrtr (io/writer realpath)]
      (loop [listset ls]
        (if (not (empty? listset))
          (let [thisls (first listset)
                [filepath & tuples] thisls ;destructing
                lines (check-keyword-content tuples keyword)] ;cache results first
            (if keyword
              ;;if keyword exsit and not empty lines
              (if (not (empty? lines))
                (do (.write wrtr (str "* " filepath "\n\n"))
                    (doall (doseq [line (check-keyword-content tuples keyword)]
                             (.write wrtr (str "** " keyword " " (format-content line)) "\n")))
                    (.write wrtr "\n")))
              ;;if not keyword input, write all
              (do (.write wrtr (str "* " filepath "\n\n"))
                  (doseq [line tuples]
                    (.write wrtr
                            (str "** "
                                 (format "%s (in line %d)\n" (second line) (first line))
                                 "\n")))
                  (.write wrtr "\n")))
            (recur (rest listset)))))
      )))
