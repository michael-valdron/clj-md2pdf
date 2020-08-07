(defproject clj-md2pdf "0.1.0"
  :description ""
  :url "https://github.com/michael-valdron/clj-md2pdf"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [org.clojure/tools.cli "1.0.194"]
                 [markdown-to-hiccup "0.6.2"]
                 [clj-htmltopdf "0.1-alpha7"]
                 [eftest "0.5.9"]]
  :plugins [[lein-eftest "0.5.9"]]
  :repl-options {:init-ns clj-md2pdf.core}
  :main clj-md2pdf.core)
