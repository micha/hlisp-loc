(ns loc.utils)

(defn log [& args]
  (.apply (.-log js/console) js/console
          (into-array (map clj->js args))))
