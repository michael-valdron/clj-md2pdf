(ns clj-md2pdf.main
  (:gen-class)
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [clojure.tools.cli :refer [parse-opts]]
            [clj-md2pdf.core :refer [gen-pdf-from-files]]))

(def file-validator
  [#(.. (io/as-file %) (exists)) "File must exists."])

(def cli-options
  [[nil "--doc DOC" "The edn file with base document attributes."
    :validate file-validator]
   [nil "--page PAGE" "The edn file with base page attributes."
    :validate file-validator]
   [nil "--styles STYLES" (str "The edn file with style attributes. "
                               "(Optional) You can specify the following map within your edn file:\n"
                               "{:styles [\"style.css\" ..]\n"
                               " :fonts {:font-family \"<font-family>\"\n"
                               "         :src \"/path/to/font.ttf\"}}")
    :validate file-validator]
   [nil "--watermark WATERMARK" "When specified creates a watermark with the attributes in the given edn."
    :validate file-validator]
   [nil "--objects OBJECTS" "The edn file with multiple object attributes to create graphics."
    :validate file-validator]
   ["-d" "--debug" "Enables debug mode."]
   ["-l" "--logging" "Enables logging"]
   ["-h" "--help" "Shows summary of the usage."]])

(defn -main [& args]
  (let [arg-opts (parse-opts args cli-options)]
    (cond
      (:errors arg-opts) (println (string/join "\n" (:errors arg-opts)))
      (get-in arg-opts [:options :help]) (println (:summary arg-opts))
      :else (if (< (count (:arguments arg-opts)) 2)
              (println "Missing required arguments 'out' and/or 'in'.")
              (let [[out & ins] (:arguments arg-opts)]
                (println "Rendering PDF..")
                (if (gen-pdf-from-files out ins (:options arg-opts))
                  (println "done.")
                  (println "failed.")))))))
