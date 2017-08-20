(ns codeitlater.core
  (:require [clojure.java.io :as io])
  (:gen-class))

(def testMark "//")
(def testKeyword "TODO") ; using keyword to filter which is which

(defn make-pattern [commentMark]
  (re-pattern (str commentMark ".+")))

;TODO: delete commentMark and filter different type of comment
(defn read-comments-inline [pattern line]
  (let [result (re-find pattern line)]
    result)) 

(defn read-comments-in-file [filepath pickcomment] ;pickcomment is curry function
  (with-open [codefile (clojure.java.io/reader filepath)]
    (let [count (atom 0)]
      (for [thisline (doall (line-seq codefile))
            :let [comment (pickcomment thisline)
                  lineNum (swap! count inc)]
            :when comment]
        (list lineNum comment))
      )))

(defn get-all-files
  ([]
   (doall (map #(.getPath %) (file-seq (io/file ".")))))
  ([root]
   (doall (map #(.getPath %) (file-seq (io/file root)))))
  ([root & filetypes]
   (let [typepatterns (for [filetype filetypes] (re-pattern (str filetype "$")))]
     (for [thisfile (doall (file-seq (io/file root)))
           typepattern typepatterns
           :let [filename (.getPath thisfile)]
           :when (re-find typepattern filename)]
       filename)))
  )

(defn -main [& args]
  (println args)
  (println (read-comments-inline (make-pattern testMark) "aaa//test"))
  (println (read-comments-in-file "/Users/ccQ/Desktop/log"
                                  (partial read-comments-inline (make-pattern testMark)))))
