(ns codeitlater.core
  (:gen-class))

(def testMark "//")

(defn make-pattern [commentMark]
  (re-pattern (str commentMark ".+")))

(defn read-comments-inline [pattern line]
  (let [result (re-find pattern line)]
    result))

(defn read-file [filepath]
  (with-open [codefile (clojure.java.io/reader filepath)]
    (doseq [thisline (line-seq codefile)]
      (println (read-comments-inline (make-pattern testMark) thisline))
      )))

(defn -main []
  (println (read-comments-inline (make-pattern testMark) "aaa//test")))
