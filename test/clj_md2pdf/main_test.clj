(ns clj-md2pdf.main-test
  (:require [clojure.test :refer :all]
            [clj-md2pdf.main :refer :all]))

(def doc-out "resources/pdf/doc.pdf")
(def one-md-in "resources/md/doc.md")
(def doc-metadata "resources/edn/doc.edn")

(deftest test-one-file-run
  (testing
   (do (-main doc-out one-md-in)
       (is true))))

(deftest test-one-file-with-attrs-run
  (testing (do (-main doc-out one-md-in "--doc" doc-metadata)
               (is true))))
