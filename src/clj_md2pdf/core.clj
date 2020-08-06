(ns clj-md2pdf.core
  (:require [clojure.java.io :as io]
            [clj-htmltopdf.core :as htmltopdf]
            [markdown-to-hiccup.core :refer [file->hiccup]]))

;; TODO: Write other functions

(defn ->pdf
  "Wrapper for `->pdf` in `clj-htmltopdf` which creates parent directories if non-existant."
  [in out & options]
  (io/make-parents out)
  (-> htmltopdf/->pdf
      (partial in out)
      (apply options)))

(defn -main [& args]
  ;; TODO: Write main function
  (assert false "Not Implemented."))
