(ns loc
  (:require-macros
   [hlisp.macros                 :refer [tpl]]
   [hlisp.reactive.macros        :refer [reactive-attributes]]
   [tailrecursion.javelin.macros :refer [cell]])
  (:require
   [clojure.string  :as s]
   [hlisp.reactive  :as d :refer [thing-looper]]
   [hlisp.util            :refer [pluralize]]
   [hlisp.env       :as e :refer [add-initfn!]]
   [tailrecursion.javelin :refer [route*]]
   [loc.utils             :refer [log]]))

;;; utils

(defn clean [value]
  (-> value
      (s/replace "\u2019" "'")
      (s/replace "&nbsp;" " ")
      ;; &amp;, etc
      (s/replace #"&\w+;" "")
      ;; First/Second street (like a corner)
      (s/replace #"([^,0-9 ]+)/[^,0-9 ]+" "$1")
      ;; Address: some meaningful stuff
      (s/replace #"[^:]+:" "")
      ;; (some description)
      (s/replace #"\(.*?\)" "")))

;;; locations

(def phase (cell :edit))
(def addresses (cell '[]))

(defn set-addresses! [text]
  (reset! addresses (s/split text #"\n")))

(defn search! [e]
  (.log js/console @addresses))


;;; maps
(def mapi (cell '{}))

(defn google-map [container]
  (let [cloned (clone container)]
    (add-initfn!
     (fn []
       (let [opts {:mapTypeId google.maps.MapTypeId.ROADMAP
                   :zoom 12}
             el (aget (d/dom-get cloned) 0)
             m (google.maps.Map. el (clj->js opts))]
         (reset! mapi {:map m
                       :coder (google.maps.Geocoder.)
                       :city-name "Kiev, Ukraine"}))))
    cloned))


(defn find-map-position! [{:keys [map coder city-name] :as qwe}]
  (when city-name ;; this should not happen
    (log "finding position")
    (.geocode
     coder
     (clj->js {:address city-name})
     (fn [[city] status]
       (swap! mapi assoc-in [:city] city)))))

(defn position-map! [{:keys [map city]}]
  (when city
    (log "panning map")
    (.panTo map
            (-> city .-geometry .-location))))

(cell (when (mapi :city-name)
        (find-map-position! mapi)))

(cell (when (mapi :city)
        (position-map! mapi)))

;; (cell (let [{:keys [map city]} mapi]
;;         (when city
;;           (log "panning map" map city)
;;           (.panTo map
;;                   (-> city .-geometry .-location)))))
