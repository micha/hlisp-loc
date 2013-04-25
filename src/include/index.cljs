(ns loc
  (:require-macros
   [hlisp.macros                 :refer [tpl def-values]]
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

;;; maps

(defn find-map-position! [coder city-name city-cell]
  (when (and coder city-name) 
    (log "finding position")
    (.geocode
      coder
      (clj->js {:address city-name})
      (fn [[city] status]
        (reset! city-cell city)))))

(defn position-map! [goog-map city]
  (when (and goog-map city) 
    (log "panning map")
    (.panTo goog-map (-> city .-geometry .-location))))

(defn make-google-map []
  (let [goog-map      (cell nil)
        map-city      (cell nil)
        map-city-name (cell nil)
        map-coder     (cell nil)]
    [map-city-name 
     (fn [attrs & _] 
       (let [container (clone (div attrs))]
         (add-initfn!
           (fn []
             (let [opts {:mapTypeId (or (:type attrs) google.maps.MapTypeId.ROADMAP)
                         :zoom ((fnil js/parseInt 0) (:zoom attrs))}
                   el (aget (d/dom-get container) 0)
                   m (google.maps.Map. el (clj->js opts))]
               (reset! goog-map m)
               (reset! map-city-name (or (:city attrs) "Kiev, Ukraine"))
               (reset! map-coder (google.maps.Geocoder.)))))
         (cell (find-map-position! map-coder map-city-name '(cell map-city)))
         (cell (position-map! goog-map map-city))
         container))]))

(def-values [map-city-name google-map] (make-google-map))


;; (cell (let [{:keys [map city]} mapi]
;;         (when city
;;           (log "panning map" map city)
;;           (.panTo map
;;                   (-> city .-geometry .-location)))))
