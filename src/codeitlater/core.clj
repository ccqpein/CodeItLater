(ns codeitlater.core
  (:gen-class))

(defn -main []
  (println "Hello, World!"))

(defn make-pattern [commentMark]
  (re-pattern (str commentMark ".+")))

(defn read-comments-inline [pattern line]
  (let [result (re-find pattern line)]
    result))

