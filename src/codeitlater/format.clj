(ns codeitlater.format
  (:require [clojure.string :as str])
  (:require [clojure.java.io :as io])
  )


(defn printline [filepath tuples]
  "tuple => ((linenum string)...) ;;from no keyword branch
OR tuple => ((linenum [keyword content])...)"
  (do
    (printf "|-- %s\n" filepath)
    (doall (map #(printf "  |-- Line %d: %s\n"
                         (first %)
                         (if (= java.lang.String (type (second %)))
                           (second %)
                           (clojure.string/join " " (second %))))
                tuples))
    (println)))


(defn split-keywords [keywords]
  ;;:= TODO: test
  ;;:= MARK: test
  "if input several keywords, should cut it to \"(keyword1|keyword2)\".
Or, just return \"keyword\""
  (if (nil? keywords)
    nil
    (let [tempResult (str/split keywords #" ")]
      (if (> (count tempResult) 1)
        (clojure.pprint/cl-format nil "(~{~a~^|~})" tempResult)
        (first tempResult)))))


(defn check-keyword-content [tuples keyword]
  "cut content those including keyword:
return ((linenum \"keyword content\")..)
"
  (let [filter-content (filter #(-> (str "(?<=" (split-keywords keyword) ":" ").*$")
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
          ;;(print-with-keyword filepath tuples keyword)
          (printline filepath (check-keyword-content tuples keyword))
          (printline filepath tuples)
          )
        ;;(println)
        (recur (rest listset))
        ))))


(defn format-content [line]
  "line struct is '(1 [keyword content])"
  (clojure.pprint/cl-format nil "~{~a ~}(in line ~a)\n" (second line) (first line))
  )


;;:= TODO: cannot accept several keywords, fix it
;;:= TEST: test
(defn write-keyword-sentence [ls keyword path]
  "write keyword content in level"
  (let [realpath (if (.isDirectory (io/file path))
                   (do (printf "%s is a directory, write in $(pwd)/project.org"
                               path)
                       (str path "/project.org"))
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
                    (doseq [line lines]
                      (.write wrtr (str "** " (format-content line) "\n")))
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
