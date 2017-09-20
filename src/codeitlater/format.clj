(ns codeitlater.format
  )

(defn list2tree [ls]
  "make list to tree"
  (loop [listset ls]
    (cond (not (empty? ls))
          (let [thisls (first listset)
                [filepath & tuples] thisls]
            (print "a")
            (format "|-- %s\n" filepath)
            (doseq [tuple tuples]
              (format " |-- %s\n" tuple))
            (recur (rest listset)))
          :else
          (println ""))))
