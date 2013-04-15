(defproject
  loc "0.1.0-SNAPSHOT"
  :description  "locations"
  :license      {:name "Eclipse Public License"
                 :url "http://www.eclipse.org/legal/epl-v10.html"}
  :source-paths ["src/clj"]
  :plugins      [[lein-hlisp "1.0.0"]]
  :dependencies [[hlisp-macros "1.0.0"]
                 [hlisp-util "0.1.0-SNAPSHOT"]
                 [hlisp-reactive "1.0.0-SNAPSHOT"]
                 [alandipert/storage-atom "1.1.1"]]
  :eval-in      :leiningen
  :hlisp        {:html-src    "src/html"
                 :cljs-src    "src/cljs"
                 :html-out    "resources"
                 :base-dir    nil
                 :cljsc-opts  {:pretty-print   true
                               :optimizations  :whitespace}})
