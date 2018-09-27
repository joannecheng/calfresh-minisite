(ns calfresh-minisite.quote-map
  (:require-macros [cljs.core.async.macros :refer [go]]
                   [clojure.string :as str])
  (:require [cljsjs.mapbox]
            [cljsjs.ScrollMagic]

            [ajax.core :refer [GET POST]]
            [clojure.string :as string]

            [calfresh-minisite.quotes :as quotes]
            [calfresh-minisite.utils :as utils]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; State
(def hovered-state-id (atom nil))

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
        ;;(.addIndicators)
        (.on "start end" action))))

(defn scroll-handlers [quote-map]
  (let [controller (js/ScrollMagic.Controller.)]
    (scroll-action "title" (partial title-action quote-map) controller)
    (scroll-action "section1" (partial san-fran quote-map) controller)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Quotes Event Handlers

;;TODO USE HICCUP GODDAMMIT THIS IS SO BAD
(defn update-title [county-name]
  (let [el (.getElementById js/document "quote_county_name")]
    (set! (.-innerHTML el) (str "<h2>" county-name "</h2>"))))

(defn county-info-row [county-data row-name item-name]
  (let [item (get county-data item-name)]
    (str "<div class=\"grid-item width-two-thirds\">"
         row-name
         "</div><div class=\"grid-item width-one-third end-row\">"
         item
         "</div>")))

(defn county-info [county-name]
  (let [county-data (get quotes/quotes county-name)]
    (str "<div class=\"grid-box county-info\">"
         (county-info-row county-data "Population" :population)
         (county-info-row county-data "Min Cost of Living (2 Working Adults, 2 Children)"
                          :minimum-cost-living-family)
         (county-info-row county-data "Median Income" :median-income)
         (county-info-row county-data "Poverty Rate" :poverty-rate)
         "</div>")))

(defn quote-html [county-quote]
  (str "<blockquote class=\"animated fadeInDown\">"
       county-quote
       "</blockquote>"
       ;;"<div class=\"profile-image\">Image here</div>"
       ))

(defn show-quotes [county-name]
  (let [county-quotes (get (get quotes/quotes county-name) :quotes)
        el (.getElementById js/document "quote_list")]
    (set! (.-innerHTML el) "")

    (if (> (count county-quotes) 0)
      (set! (.-innerHTML el) (str (county-info county-name)
                              (string/join " " (map quote-html county-quotes)))))))


(defn update-quotes [county-name]
  (update-title county-name)
  (show-quotes county-name))

(defn set-county-hover [container state-id hover-bool]
  (-> container
      (.setFeatureState #js {:source "counties" :id state-id}
                        #js {:hover hover-bool})))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Drawing
(defn draw-california
  ;; This is complicated because of Mapbox Gl's DSL
  ;; See https://www.mapbox.com/mapbox-gl-js/example/hover-styles/ for more
  ;; information on what's going on
  [quote-map-container resp]

  (-> quote-map-container
      (.addSource "counties", (clj->js {:type "geojson" :data resp})))
  (-> quote-map-container
      (.addLayer (clj->js {:id "california-county-fill"
                           :type "fill"
                           :source "counties"
                           :layout {}
                           :paint {:fill-color "#e25050"
                                   :fill-opacity ["case"
                                                  ["boolean" ["feature-state" "hover"] false]
                                                  0.6 0]}})))

  (-> quote-map-container
      (.addLayer (clj->js {:id "california-counties"
                           :type "line"
                           :source "counties"
                           :paint {:line-color "#ccc"
                                   :line-width 1}})))

  (-> quote-map-container
      (.on "click", "california-county-fill",
           #(let [county-id (.-id (first (.-features %)))
                  county-name (.-name (.-properties (first (.-features %))))]
                (update-quotes county-name))))

  (-> quote-map-container
      (.on "mousemove", "california-county-fill",
           #(let [county-id (.-id (first (.-features %)))
                  county-name (.-name (.-properties (first (.-features %))))]
              (if (some? @hovered-state-id)
                (set-county-hover quote-map-container
                                  @hovered-state-id false))
              (reset! hovered-state-id county-id)
              (set-county-hover quote-map-container
                                county-id true))))

  (-> quote-map-container
      (.on "mouseleave", "california-county-fill",
           (fn []
             (if (some? @hovered-state-id)
               (set-county-hover quote-map-container
                                 @hovered-state-id false))
             (reset! hovered-state-id nil))))
  )

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
    ;;(scroll-handlers quote-map-container)
    (GET "./data/california-counties.json"
         :response-format :json
         :handler (partial draw-california quote-map-container))))
