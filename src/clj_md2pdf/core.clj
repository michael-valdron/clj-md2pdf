(ns clj-md2pdf.core
  (:gen-class)
  (:require [clojure.java.io :as io]
            [clojure.tools.cli :refer [parse-opts]]
            [clojure.string :as string]
            [clojure.set :refer [rename-keys]]
            [clojure.spec.alpha :as spec]
            [clj-htmltopdf.core :as htmltopdf]
            [markdown-to-hiccup.core :refer [file->hiccup hiccup-in]]))

(declare read-md ->pdf)

(def spec-pdf (spec/and string? #(re-matches #"^.*\.pdf$" %)))
(def spec-md (spec/and string? #(re-matches #"^.*\.md$" %)))
(def spec-edn (spec/and string? #(re-matches #"^.*\.edn$" %)))
(def spec-edn-options (spec/map-of keyword? spec-edn))

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

(defn get-body-contents
  [hiccup]
  (->> (hiccup-in hiccup :body)
       (drop 2)))

(defn hiccup-out
  [hiccup]
  (->> hiccup
       (into [:body {}])
       (conj [:html {} [:head {}]])))

(defn update-debug
  [options]
  (if (nil? (:debug options))
    options
    (update options :debug #(hash-map :display-html? %
                                      :display-options? %))))

(defn parse-edn-opts
  [edn-options]
  (assert (spec/valid? spec-edn-options edn-options)
          "Invalid EDN files found.")
  edn-options)

(defn parse-options
  [options]
  (let [option-attrs (->> [:doc :page :styles :watermark :objects]
                          (select-keys options)
                          (parse-edn-opts)
                          (reduce-kv #(assoc %1 %2 ((comp read-string slurp) %3)) {})
                          (into (select-keys options [:debug :logging])))]
    (-> option-attrs
        (rename-keys {:logging :logging?})
        (update-debug))))

(defn render-pdf
  [out ins options]
  (let [hiccup (apply read-md ins)]
    (->> options
         (parse-options)
         (->pdf hiccup out))))

(defn read-md
  "Reads the markdown file(s) into hiccup data structures."
  [in & ins]
  (assert (and (spec/valid? spec-md in)
               (every? #(spec/valid? spec-md %) ins))
          "Invalid markdown files found.")
  (->> (into [in] ins)
       (mapcat (comp get-body-contents file->hiccup))
       (hiccup-out)))

(defn ->pdf
  "Wrapper for `->pdf` in `clj-htmltopdf` which creates parent directories 
   if non-existant."
  [hiccup out options]
  (assert (spec/valid? spec-pdf out) "PDF filename invalid.")
  (io/make-parents out)
  (if (empty? options)
    (htmltopdf/->pdf hiccup out)
    (htmltopdf/->pdf hiccup out options)))

(defn -main [& args]
  (let [arg-opts (parse-opts args cli-options)]
    (cond
      (:errors arg-opts) (println (string/join "\n" (:errors arg-opts)))
      (get-in arg-opts [:options :help]) (println (:summary arg-opts))
      :else (if (< (count (:arguments arg-opts)) 2)
              (println "Missing required arguments 'out' and/or 'in'.")
              (let [[out & ins] (:arguments arg-opts)]
                (println "Rendering PDF..")
                (render-pdf out ins (:options arg-opts))
                (println "done.")
                (shutdown-agents))))))
