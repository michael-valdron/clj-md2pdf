(ns clj-md2pdf.core-test
  (:require [clojure.test :refer :all]
            [clojure.spec.alpha :as spec]
            [markdown-to-hiccup.core :refer [file->hiccup]]
            [clj-md2pdf.core :refer :all]))

(def doc-out "resources/pdf/doc.pdf")
(def one-md-in "resources/md/doc.md")
(def doc-metadata "resources/edn/doc.edn")
(def page-options "resources/edn/page.edn")
(def styles-props "resources/edn/styles.edn")
(def objects-renderers "resources/edn/objects.edn")

;; Library access tests
(deftest test-markdown
  (testing
   (is (-> (file->hiccup one-md-in)
           (comp not empty?)))))

(deftest test-pdf
  (testing
   (is (-> (file->hiccup one-md-in)
           (->pdf doc-out {})
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
   (let [edn-map {:doc doc-metadata
                  :debug true}
         sol-map {:doc  (-> (:doc edn-map)
                            (slurp)
                            (read-string))
                  :debug {:display-html? true
                          :display-options? true}}]
     (is (= sol-map (read-options edn-map))))))

;; Integration Tests
(deftest test-one-file-run
  (testing
   (let [result (gen-pdf-from-files doc-out [one-md-in] {})]
     (is (= true result)))))

(deftest test-one-file-with-attrs-run
  (testing
   (let [options {:doc doc-metadata
                  :page page-options
                  :styles styles-props
                  :objects objects-renderers}
         result (gen-pdf-from-files doc-out [one-md-in] options)]
     (is (= true result)))))
