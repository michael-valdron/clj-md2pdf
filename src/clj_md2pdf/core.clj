(ns clj-md2pdf.core
  (:require [clojure.java.io :as io]
            [clojure.set :refer [rename-keys]]
            [clojure.spec.alpha :as spec]
            [clj-htmltopdf.core :as htmltopdf]
            [markdown-to-hiccup.core :refer [file->hiccup hiccup-in]]))

(def spec-pdf (spec/and string? #(re-matches #"^.*\.pdf$" %)))
(def spec-md (spec/and string? #(re-matches #"^.*\.md$" %)))
(def spec-edn (spec/and string? #(re-matches #"^.*\.edn$" %)))
(def spec-edn-options (spec/map-of keyword? spec-edn))

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

(defn read-options
  [options]
  (let [option-attrs (->> [:doc :page :styles :watermark :objects]
                          (select-keys options)
                          (parse-edn-opts)
                          (reduce-kv #(assoc %1 %2 ((comp read-string slurp) %3)) {})
                          (into (select-keys options [:debug :logging])))]
    (-> option-attrs
        (rename-keys {:logging :logging?})
        (update-debug))))

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

(defn render-pdf-from-files
  [out ins options]
  (let [hiccup (apply read-md ins)]
    (->> options
         (read-options)
         (->pdf hiccup out))))
