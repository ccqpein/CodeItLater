(ns codeitlater.parser-args-test
  (:require [clojure.test :refer :all]
            [codeitlater.parser-args :refer :all]
            [clojure.tools.cli :refer [parse-opts]])
  )

(deftest parser-comments-test
  (testing "Test parser"
    (is (= {:dir "." :filetype nil :json "https://raw.githubusercontent.com/ccqpein/codeitlater/master/src/codeitlater/comments.json" :jsonx nil}
           (get (parse-opts '("-d" ".") codeitlater.parser-args/command) :options)))
    (is (= {:dir "." :filetype "clj" :json "https://raw.githubusercontent.com/ccqpein/codeitlater/master/src/codeitlater/comments.json" :jsonx nil}
           (get (parse-opts '("-d" "." "-f" "clj") codeitlater.parser-args/command) :options)))
    (is (= {:dir "." :filetype "clj go" :json "https://raw.githubusercontent.com/ccqpein/codeitlater/master/src/codeitlater/comments.json" :jsonx nil}
           (get (parse-opts '("-f" "clj go") codeitlater.parser-args/command) :options)))
    (is (= {:dir "." :filetype "clj go" :json "https://raw.githubusercontent.com/ccqpein/codeitlater/master/src/codeitlater/comments.json" :jsonx nil}
           (get (parse-opts '("-f" "clj go" "-d" ".") codeitlater.parser-args/command) :options)))
    (is (= {:dir "." :filetype "clj go" :json "https://raw.githubusercontent.com/ccqpein/codeitlater/master/src/codeitlater/comments.json" :jsonx nil}
           (get (parse-opts '("-f" "clj go" "-d" ".") codeitlater.parser-args/command) :options)))
    ))

