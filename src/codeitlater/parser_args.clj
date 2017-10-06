(ns codeitlater.parser-args
  (:require [clojure.tools.cli :refer [parse-opts]])
  (:gen-class))

;; https://github.com/clojure/tools.cli
(def command
  [["-d" "--dir DIR" "Root dir"
    :default "."
    ]
   ["-f" "--filetype FILETYPE" "File types"
    :id :filetype
    :default nil
    ]
   ["-j" "--json JSON" "Comment json file"
    :default "https://raw.githubusercontent.com/ccqpein/codeitlater/master/src/codeitlater/comments.json"]
   [nil "--jsonx JSON" "Expand comment json file"
    :default nil]
   ])
