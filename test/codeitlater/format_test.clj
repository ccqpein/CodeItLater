(ns codeitlater.format-test
  (:require [clojure.test :refer :all]
            [codeitlater.format :refer :all]))

(deftest check-keyword-content-test
  (testing "Test if check keyword content return correct value"
    (is (= '((1 ["TODO" "aaa"]))
           (check-keyword-content '((1 "TODO: aaa")) "TODO")))
    (is (= '((1 ["TODO" "aaa"]))
           (check-keyword-content '((1 "TODO: aaa") (2 "aaaa")) "TODO")))
    (is (= '((1 ["TODO" "aaa"]) (3 ["TODO" "bbb"]))
           (check-keyword-content '((1 "TODO: aaa") (2 "aaaa") (3 "TODO: bbb")) "TODO")))))

(deftest )
