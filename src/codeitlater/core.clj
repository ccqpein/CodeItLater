(ns codeitlater.core
  (:require [clojure.java.io :as io])
  (:gen-class))

(def testKeyword "TODO") ;:=using keyword to filter which is which

;; This regex expression get help from: https://stackoverflow.com/questions/45848999/clojure-regex-delete-whitespace-in-pattern
(defn make-pattern [commentMark]
  (re-pattern (str commentMark "+:=\\s*" "(.+)")))


(defn read-comments-inline [pattern line]
  (let [result (re-find pattern line)]
    (second result))) 


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
  "return a lazy sequence including all files"
  ([]
   (map #(.getPath %) (file-seq (io/file "."))))
  ([root]
   (map #(.getPath %) (file-seq (io/file root))))
  )


(defn read-files ;;:= TODO: directory format
  "commentMarkFunc is a curry function to give read-comments-in-file
=> (partial read-comments-inline (make-pattern commentMark))"
  ([commentMarkFunc]
   (doall (for [filepath (get-all-files)]
            (conj (read-comments-in-file filepath
                                         commentMarkFunc)
                  filepath))))
  ([commentMarkFunc root]
   (doall (for [filepath (get-all-files root)]
            (conj (read-comments-in-file filepath
                                         commentMarkFunc)
                  filepath))))
  ([commentMarkFunc root & filetypes]
   (let [typepatterns (for [filetype filetypes
                            :when (not= "" filetype)]
                        (re-pattern (str ".+" filetype "$")))]
     (doall (for [filepath (get-all-files root)
                  typepattern typepatterns
                  :when (re-matches typepattern filepath)]
              (conj (read-comments-in-file filepath
                                           commentMarkFunc)
                    filepath))))))


(defn -main [& args]
  (println args)
  (println (read-files (partial read-comments-inline (make-pattern ";")) "./src" "clj" ""))
)
