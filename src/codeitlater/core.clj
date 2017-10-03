(ns codeitlater.core
  (:require [clojure.java.io :as io]
            [clojure.data.json :as json]
            [clojure.tools.cli :refer [parse-opts]]
            [codeitlater.format :as cilformat]
            [codeitlater.parser-args :refer :all]
            [clojure.string :as str]
            )
  (:gen-class))

(defn read-json [path]
  (json/read-str (slurp path)))


;; This regex expression get help from: https://stackoverflow.com/questions/45848999/clojure-regex-delete-whitespace-in-pattern
(defn make-pattern [commentMark]
  (re-pattern (str commentMark "+:=\\s*" "(.+)")))


(defn read-comments-inline [commentMark line]
  (let [result (re-find (make-pattern commentMark) line)]
    (second result))) 


(defn read-comments-in-file [filepath commentMark]
  (with-open [codefile (io/reader filepath)]
    (let [count (atom 0)
          pickcomment (partial read-comments-inline commentMark)]
      (for [thisline (doall (line-seq codefile))
            :let [comment (pickcomment thisline)
                  lineNum (swap! count inc)] ;;:= atom?
            :when comment]
        (list lineNum comment))
      )))


(defn get-all-files [root]
  "return a lazy sequence including all files"
  (map #(.getPath %) (file-seq (io/file root))))

(defn is-hidden-file? [filepath]
  "check file is hidden file or not"
  (if (re-find #"#.*$" filepath) true nil))

;;(partial read-comments-inline commentMark)
(defn read-files
  "Read all files depend on root path and file types, find comments inside and return."
  ([commentDict root]
   (doall (for [filepath (get-all-files root)
                :when (not (or (.isDirectory (io/file filepath))
                               (is-hidden-file? filepath)))
                :let [mark (get commentDict (re-find #"(?<=\w)\.\w+$" filepath))]
                :when mark
                :let [comment (read-comments-in-file filepath mark)]
                :when (->> comment empty? not)]
            (conj comment
                  filepath))))
  ([commentDict root filetypes]
   (let [typepatterns (for [filetype filetypes
                            :when (not= "" filetype)]
                        (list (re-pattern (str ".+" filetype "$"))
                              (str "." filetype)))]
     (doall (for [filepath (get-all-files root)
                  :when (not (or (.isDirectory (io/file filepath))
                                 (is-hidden-file? filepath)))
                  typepattern typepatterns
                  :when (re-matches (first typepattern) filepath)
                  :let [mark (get commentDict (re-find #"(?<=\w)\.\w+$" filepath))]
                  :when mark
                  :let [comment (read-comments-in-file filepath (get commentDict (last typepattern)))]
                  :when (->> comment empty? not)]
              (conj comment
                    filepath))))))


(defn -main [& args]
  (let [options (-> args (parse-opts command) (get :options))
        dir (:dir options)
        filetypes (:filetype options)
        jsonpath (:json options)
        commentDict (read-json jsonpath)]
    (println options jsonpath)
    (cond filetypes (cilformat/list2tree (read-files commentDict dir (str/split filetypes #" ")))
          dir (cilformat/list2tree (read-files commentDict dir)))
    ;; https://stackoverflow.com/questions/36251800/what-is-clojures-flush-and-why-is-it-necessary
    (flush)))
