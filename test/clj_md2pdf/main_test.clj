(ns clj-md2pdf.main-test
  (:require [clojure.test :refer :all]
            [clj-md2pdf.main :refer :all]))

(def doc-out "resources/pdf/doc.pdf")
(def one-md-in "resources/md/doc.md")
(def doc-metadata "resources/edn/doc.edn")
(def page-options "resources/edn/page.edn")
(def styles-props "resources/edn/styles.edn")

(deftest test-one-file-run
  (testing
   (do (-main doc-out one-md-in)
       (is true))))

(deftest test-one-file-with-attrs-run
  (testing (do (-main doc-out one-md-in 
                      "--doc" doc-metadata
                      "--page" page-options
                      "--styles" styles-props)
               (is true))))
