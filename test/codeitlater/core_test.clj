(ns codeitlater.core-test
  (:require [clojure.test :refer :all]
            [codeitlater.core :refer :all]))

(deftest read-comment-inline-test
  (testing "Read comment in line fail"
    (is (= "// abc"
            (read-comments-inline (make-pattern "//") "aaa // abc")))))
