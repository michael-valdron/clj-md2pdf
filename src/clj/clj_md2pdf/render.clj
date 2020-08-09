(ns clj-md2pdf.render
  (:require [hiccup.page :as h]
            [clj-htmltopdf.options :refer [page-options->css
                                           build-font-face-styles]]
            [clj-htmltopdf.css :refer [css->str]])
  (:import [md2pdf.java IRenderer PdfRenderer]))


(defn- create-obj-renderer
  [f]
  (reify IRenderer
    (render [_ element-attrs g]
      (f element-attrs g))))

(defn render-body
  [styles]
  (->> (when (map? styles)
         (dissoc styles :styles :fonts))
       (conj [:body])
       (vector)))

(defn render-font
  [styles]
  (build-font-face-styles styles))

(defn render-css
  [styles]
  (-> (render-body styles)
      (concat (render-font styles))
      (vec)
      (css->str)))

(defn embed-link-tags!
  [head style-links]
  (doseq [link style-links]
    (PdfRenderer/setLinkTag head link)))

(defn embed-styles!
  [head styles]
  (PdfRenderer/setStyleTag head (render-css styles))
  (when-let [style-links (:styles styles)]
    (cond
      (sequential? style-links) (embed-link-tags! head style-links)
      (string? style-links) (embed-link-tags! head style-links))))

(defn embed-meta-tags!
  [head options]
  (let [doc (:doc options)]
    (doseq [[k v] doc]
      (PdfRenderer/setMetaTag head (name k) v))))

(defn embed-options!
  [html-doc options]
  (let [head (PdfRenderer/selectHead html-doc)
        styles (:styles options)]
    (when (:doc options)
      (embed-meta-tags! head options))
    (when (:page options)
      (PdfRenderer/setStyleTag head (-> (page-options->css (:page options))
                                        (css->str))))
    (cond
      (sequential? styles) (embed-link-tags! head styles)
      (string? styles) (embed-link-tags! head [styles])
      (or (not (nil? styles))
          (map? styles)) (embed-styles! head styles))
    html-doc))

(defn render-doc
  "Renders HTML DOC object from hiccup and EDN options"
  ([hiccup] (render-doc hiccup {}))
  ([hiccup options]
   (let [html (h/html5 {} hiccup)
         html-doc (-> (PdfRenderer/makeHtmlDoc html "")
                      (embed-options! options))]
     (when (get-in options [:debug :display-html?])
       (println (str html-doc)))
     html-doc)))

(defn render-pdf
  [html-doc out options]
  (->> (get-in options [:objects :by-id])
       (reduce-kv #(assoc %1 %2 (-> (eval %3)
                                    (create-obj-renderer))) {})
       (PdfRenderer/renderPdf html-doc "" out)))

(defn ->pdf
  "Clojure wrapper for low level Java HTML to PDF renderer."
  ([hiccup out] (->pdf hiccup out {}))
  ([hiccup out options]
   (-> (render-doc hiccup options)
     ;; TODO: Watermark rendering and logging.
       (render-pdf out options))))
