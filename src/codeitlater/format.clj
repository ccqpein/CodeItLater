(ns codeitlater.format
  )

(defn list2tree [ls keyword]
  "make list to tree"
  (loop [listset ls]
    (if (not (empty? listset))
          (let [thisls (first listset)
                [filepath & tuples] thisls]
            (printf "|-- %s\n" filepath)
            (doseq [tuple tuples]
              (if keyword
                (let [content (-> (str "(?<=" keyword ":" ").*$") (re-pattern) (re-find (second tuple)))]
                  (if content
                    (printf "  |-- %s\n" tuple)))
                (printf "  |-- %s\n" tuple)))
            (println)
            (recur (rest listset))
            ))))

