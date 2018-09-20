(ns calfresh-minisite.quote-map
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljsjs.mapbox]
            [cljsjs.ScrollMagic]

            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]

            [calfresh-minisite.utils :as utils]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Scroll Actions
(defn title-action [quote-map]
  (.easeTo quote-map (clj->js {:center [-119.4179 36.772537]
                               :zoom 2})))

(defn san-fran [quote-map]
  (.easeTo quote-map (clj->js {:center [-122.420679 37.772537]
                               :zoom 12})))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Event Handling

(defn scroll-action [element-id action controller]
  (let [el-height (utils/height-of element-id)]
    (-> (js/ScrollMagic.Scene.
         #js {:triggerElement (str "#" element-id) :duration (- el-height 80)})
        (.addTo controller)
        (.addIndicators)
        (.on "start end" action))))

(defn scroll-handlers [quote-map]
  (let [controller (js/ScrollMagic.Controller.)]
    (scroll-action "title" (partial title-action quote-map) controller)
    (scroll-action "section1" (partial san-fran quote-map) controller)
    )
  )

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Drawing
(defn draw-california [quote-map-container]
  (go (let [resp (<! (http/get "./data/california-counties.json"))]
        (-> quote-map-container
            (.addLayer (clj->js {:id "california_counties"
                                 :type "line"
                                 :source {:type "geojson"
                                          :data (:body resp)}
                                 :paint {:line-color "#ccc"
                                         :line-width 1}
                                 })))
        )))

(defn draw-map []
  (js/mapboxgl.Map.
   #js {:container "quote_map"
        :style "mapbox://styles/mapbox/dark-v9",
        :maxBounds (js/mapboxgl.LngLatBounds.
                    #js [-124.48200988, 31.52952194]
                    #js [-114.13077545, 43.00950241])
        }))

(defn draw []
  (let [quote-map-container (draw-map)]
    (scroll-handlers quote-map-container)
    (draw-california quote-map-container)))
