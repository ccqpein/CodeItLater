(ns codeitlater.format
  )

(defn printline [filepath tuples]
  (do
    (printf "|-- %s\n" filepath)
    (doall (map #(printf "  |-- %s\n" %) tuples))
    (println)))

(defn print-with-keyword [filepath tuples keyword]
  (let [filter-content (filter
                        #(-> (str "(?<=" keyword ":" ").*$") (re-pattern) (re-find (second %)))
                        tuples)]
    (if (not (empty? filter-content))
      (printline filepath filter-content)
      )))

(defn list2tree [ls keyword]
  "make list to tree"
  ;:= print for test
  ;(println ls)
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
