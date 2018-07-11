(ns codeitlater.core
  (:require [clojure.java.io :as io]
            [clojure.data.json :as json]
            [clojure.tools.cli :refer [parse-opts]]
            [codeitlater.format :as cilformat]
            [codeitlater.parser-args :refer :all]
            [clojure.string :as str]
            )
  (:gen-class))


(def ^:dynamic *help-url* "https://raw.githubusercontent.com/ccqpein/codeitlater/master/doc/help")


(defn read-json [^String path]
  (if path (json/read-str (slurp path)) nil))


;; This regex expression get help from: https://stackoverflow.com/questions/45848999/clojure-regex-delete-whitespace-in-pattern
(defn make-pattern [commentmark]
  "make regex pattern by different languages' comment start symbol"
  (re-pattern (str commentmark "+:=\\s*" "(.+)")))


(defn read-comments-inline [commentmark line]
  (let [result (re-find (make-pattern commentmark) line)]
    (second result))) 


(defn read-comments-in-file [filepath commentmark]
  (if (nil? commentmark) '()
    (with-open [codefile (io/reader filepath)]
      (let [count (atom 0)
            pickcomment (partial read-comments-inline commentmark)] ; partial functino here
        (for [thisline (doall (line-seq codefile))
              :let [comment (pickcomment thisline)
                    linenum (swap! count inc)]
              :when comment]
          (list linenum comment))))
    ))


(defn get-all-files [root]
  "return a lazy sequence including all files"
  (map #(.getPath %) (file-seq (io/file root))))


(defn is-hidden-file? [filepath]
  "check file is hidden file or not"
  (if (re-find #"#.*$" filepath) true nil))


(defn read-files
  "Read all files depend on root path and file types, find comments inside and return."
  ([commentdict root]
   (reduce conj '()
           (filter #(> (count %) 1)
                   (map #(conj
                          (->> %
                               (re-find #"(?<=\w)\.\w+$")
                               (get commentdict)
                               (read-comments-in-file %))
                          %)
                        (filter #(not (or (.isDirectory (io/file %))
                                          (is-hidden-file? %)))
                                (get-all-files root))))))
  ([commentdict root filetypes]
   (let [typepatterns
         (for [filetype filetypes
               :when (not= "" filetype)]
           (list (re-pattern (str ".+" filetype "$"))
                 (str "." filetype)))]
     (reduce concat
             (for [typepattern typepatterns]
               (filter #(> (count %) 1)
                       (map #(conj
                              (->> %
                                   (re-find #"(?<=\w)\.\w+$")
                                   (get commentdict)
                                   (read-comments-in-file %))
                              %)
                            (filter #(and
                                      (not (or (.isDirectory (io/file %))
                                               (is-hidden-file? %)))
                                      (re-matches (first typepattern) %))
                                    (get-all-files root)))))))
   ))


(defn -main [& args]
  (let [options (-> args (parse-opts command) (get :options))
        {dir :dir 
         filetypes :filetype 
         jsonpath :json 
         jsonpathx :jsonx
         keyword :keyword
         help :help} options
        commentdict (into (read-json jsonpath) (read-json jsonpathx))]
    ;(println commentdict)
    ;(println options)
    (cond
      help (with-open [rdr (io/reader *help-url*)]
             (doseq [line (line-seq rdr)]
               (println line)))
      ; when have file type(s)
      filetypes (cilformat/list2tree
                 (read-files commentdict dir (str/split filetypes #" "))
                 keyword)
      ; when want to scan different dir
      dir (cilformat/list2tree
           (read-files commentdict dir)
           keyword))
    ;; https://stackoverflow.com/questions/36251800/what-is-clojures-flush-and-why-is-it-necessary
    (flush)))
