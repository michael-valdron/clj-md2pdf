(defproject clj-md2pdf "0.1.0"
  :description ""
  :url "https://github.com/michael-valdron/clj-md2pdf"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [org.clojure/tools.cli "1.0.194"]
                 [hiccup "1.0.5"]
                 [markdown-to-hiccup "0.6.2"]
                 [clj-htmltopdf "0.1-alpha7"]
                 [org.jsoup/jsoup "1.13.1"]
                 [com.openhtmltopdf/openhtmltopdf-core "1.0.4"]
                 [com.openhtmltopdf/openhtmltopdf-pdfbox "1.0.4"]
                 [com.openhtmltopdf/openhtmltopdf-svg-support "1.0.4"]
                 [eftest "0.5.9"]]
  :plugins [[lein-eftest "0.5.9"]]
  :source-paths ["src/clj"]
  :java-source-paths ["src/java"]
  :repl-options {:init-ns clj-md2pdf.core}
  :main clj-md2pdf.main)
