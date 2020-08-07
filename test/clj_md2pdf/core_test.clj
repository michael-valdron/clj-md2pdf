(ns clj-md2pdf.core-test
  (:require [clojure.test :refer :all]
            [clojure.spec.alpha :as spec]
            [markdown-to-hiccup.core :refer [file->hiccup]]
            [clj-md2pdf.core :refer :all]))

;; Library access tests
(deftest test-markdown
  (testing
   (is (-> (file->hiccup "resources/md/doc.md")
           (comp not empty?)))))

(deftest test-pdf
  (testing
   (is (-> (file->hiccup "resources/md/doc.md")
           (->pdf "resources/pdf/doc.pdf" {})
           (comp not nil?)))))

;; Validation tests
(deftest test-pdf-validation
  (testing
   (is (= true (spec/valid? spec-pdf "path/to/a/pdf/file.pdf")))))

(deftest test-md-validation
  (testing
   (is (= true (spec/valid? spec-md "path/to/a/md/file.md")))))

(deftest test-edn-validation
  (testing
   (is (= true (spec/valid? spec-edn "path/to/a/md/file.edn")))))

(deftest test-edn-options-validation
  (testing
   (let [edn-map {:doc "path/to/doc.edn"
                  :page "path/to/page.edn"
                  :styles "path/to/styles.edn"}]
     (is (= true (spec/valid? spec-edn-options edn-map))))))

(deftest test-edn-options-invalidation
  (testing
   (let [edn-map {:doc :adoc
                  :page "path/to/page.edn"
                  :styles 2}]
     (is (= false (spec/valid? spec-edn-options edn-map))))))

;; Option Parsing
(deftest test-parse-options
  (testing
   (let [edn-map {:doc "resources/edn/doc.edn"
                  :debug true}
         sol-map {:doc  (-> (:doc edn-map)
                            (slurp)
                            (read-string))
                  :debug {:display-html? true
                          :display-options? true}}]
     (is (= sol-map (parse-options edn-map))))))