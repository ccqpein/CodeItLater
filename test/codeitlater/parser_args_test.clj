(ns codeitlater.parser-args-test
  (:require [clojure.test :refer :all]
            [codeitlater.parser-args :refer :all]
            [clojure.tools.cli :refer [parse-opts]])
  )

(deftest parser-comments-test
  (testing "Test parser"
    (is (= {:path "." :filetype nil}
           (get (parse-opts '("-p" ".") codeitlater.parser-args/command) :options)))
    (is (= {:path "." :filetype "clj"}
           (get (parse-opts '("-p" "." "-f" "clj") codeitlater.parser-args/command) :options)))))


















