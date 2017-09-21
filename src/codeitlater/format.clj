(ns codeitlater.format
  )

(defn list2tree [ls]
  "make list to tree"
  (loop [listset ls]
    (if (not (empty? listset))
          (let [thisls (first listset)
                [filepath & tuples] thisls]
            (printf "|-- %s\n" filepath)
            (doseq [tuple tuples]
              (printf "  |-- %s\n" tuple))
            (recur (rest listset)))
          )))

