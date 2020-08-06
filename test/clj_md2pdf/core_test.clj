(ns clj-md2pdf.core-test
  (:require [clojure.pprint :refer [pprint]]
            [clojure.test :refer :all]
            [markdown-to-hiccup.core :refer [file->hiccup]]
            [clj-md2pdf.core :refer :all]))

(deftest test-markdown
  (testing
   (do (println "\nTest #1:")
       (-> (doto (file->hiccup "resources/md/doc.md")
             (pprint))
           (comp #(is %) not empty?)))))

(deftest test-pdf
  (testing
   (do (println "\nTest #2:")
       (-> (file->hiccup "resources/md/doc.md")
           (->pdf "resources/pdf/doc.pdf" {:debug {:display-html? true}})
           (comp #(is %) not nil?)))))
