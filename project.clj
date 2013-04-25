(defproject
  loc "0.1.0-SNAPSHOT"
  :description  "locations"
  :license      {:name "Eclipse Public License"
                 :url "http://www.eclipse.org/legal/epl-v10.html"}
  :source-paths ["src/clj"]
  :plugins      [[tailrecursion/lein-hlisp "2.0.0"]]
  :dependencies [[tailrecursion/hlisp-macros "1.0.0"]
                 [tailrecursion/hlisp-util "0.1.0-SNAPSHOT"]
                 [tailrecursion/hlisp-reactive "1.0.2-SNAPSHOT"]]
  :eval-in      :leiningen
  :hlisp        {:html-src    "src/html"
                 :cljs-src    "src/cljs"
                 :html-out    "resources"
                 :base-dir    nil
                 :cljsc-opts  {:pretty-print   true
                               :optimizations  :whitespace}})
