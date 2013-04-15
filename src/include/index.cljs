(ns loc
  (:require-macros
    [hlisp.macros                 :refer [tpl]]
    [hlisp.reactive.macros        :refer [reactive-attributes]]
    [tailrecursion.javelin.macros :refer [cell]])
  (:require
    [hlisp.reactive         :as d :refer [thing-looper]]
    [hlisp.util                   :refer [pluralize]]
    [tailrecursion.javelin        :refer [route*]]
    [alandipert.storage-atom      :refer [local-storage]]))

;; internal ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def phase (cell :entering))
