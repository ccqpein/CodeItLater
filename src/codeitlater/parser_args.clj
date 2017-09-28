(ns codeitlater.parser-args
  (:require [clojure.tools.cli :refer [parse-opts]])
  (:gen-class))

;; https://github.com/clojure/tools.cli
(def command
  [["-d" "--dir DIR" "Root dir"
;    :id :path
    :default "."
    ]
   ["-f" "--filetype FILETYPE" "File types"
    :id :filetype
    :default nil
    ]
   ])
