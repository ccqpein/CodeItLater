(ns codeitlater.core
  (:gen-class))

(def testMark "//")

(defn make-pattern [commentMark]
  (re-pattern (str commentMark ".+")))

;TODO: delete commentMark and filter different type of comment
(defn read-comments-inline [pattern line]
  (let [result (re-find pattern line)]
    result)) 

(defn read-comments-in-file [filepath]
  (with-open [codefile (clojure.java.io/reader filepath)]
    (let [count (atom 0)]
      (for [thisline (doall (line-seq codefile))
            :let [comment (read-comments-inline (make-pattern testMark) thisline)
                  lineNum (swap! count inc)]
            :when comment]
        (list lineNum comment))
      )))

(defn -main []
  (println (read-comments-inline (make-pattern testMark) "aaa//test")))
