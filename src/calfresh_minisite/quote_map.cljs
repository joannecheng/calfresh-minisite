(ns calfresh-minisite.quote-map
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljsjs.mapbox]
            [cljsjs.ScrollMagic]

            [ajax.core :refer [GET]]

            [calfresh-minisite.quote-view :as quote-view]
            [calfresh-minisite.utils :as utils]
            [calfresh-minisite.quotes :as quotes]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; State
(def hovered-state-id (atom 38))
(def selected-county-name (atom "San Francisco"))
(def map-animation (atom nil))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Map Panning Actions
(defn title-action [quote-map]
  (.easeTo quote-map (clj->js {:center [-119.4179 36.772537]
                               :zoom 2})))

(defn ease-to-county [quote-map]
  (let [center (get (get quotes/quotes @selected-county-name) :center)]
    (.easeTo quote-map (clj->js {:center (into-array center)
                                 :zoom 15}))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Quote map scroll handling
(defn make-quote-map-sticky []
  (let [el (.getElementById js/document "quote_map")]
    (println el)
    (-> el
        .-classList
        (.add "sticky"))))

(defn unstick-quote-map []
  (let [el (.getElementById js/document "quote_map")]
    (-> el
        .-classList
        (.remove "sticky"))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Quotes Event Handlers
(defn update-quotes []
  (let [county-name @selected-county-name]
    (quote-view/update-title county-name)
    (quote-view/show-quotes county-name)))

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

  (set-county-hover quote-map-container
                    @hovered-state-id true)

  (-> quote-map-container
      (.addLayer (clj->js {:id "california-counties"
                           :type "line"
                           :source "counties"
                           :paint {:line-color "#ccc"
                                   :line-width 1}})))
  ;; TODO: TOOLTIPS FOR COUNTIES

  (-> quote-map-container
      (.on "click", "california-county-fill",
           #(let [county-id (.-id (first (.-features %)))
                  county-name (.-name (.-properties (first (.-features %))))]
              (if (some? @hovered-state-id)
                (set-county-hover quote-map-container
                                  @hovered-state-id false))
              (reset! selected-county-name county-name)
              (set-county-hover quote-map-container county-id true)
              (reset! hovered-state-id county-id)
              (update-quotes)))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Scroll Event Handling
(defn scroll-action [element-id action trigger controller]
  (let [el-height (utils/height-of element-id)]
    (-> (js/ScrollMagic.Scene.
         #js {:triggerElement (str "#" element-id) :duration (- el-height 80)})
        (.addTo controller)
        (.on trigger action))))

(defn scroll-handlers [quote-map]
  (let [controller (js/ScrollMagic.Controller.)]
    (scroll-action "quote_map_container" make-quote-map-sticky "start" controller)
    (scroll-action "quote_map_container" unstick-quote-map "end" controller)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Main Drawing functions
(defn draw-map []
  (js/mapboxgl.Map.
   #js {:container "quote_map"
        :style "mapbox://styles/mapbox/dark-v9",
        :maxBounds (js/mapboxgl.LngLatBounds.
                    #js [-124.48200988, 31.52952194]
                    #js [-114.13077545, 43.00950241])}))

(defn draw []
  (let [quote-map-container (draw-map)]
    (update-quotes)
    (scroll-handlers quote-map-container)
    (-> quote-map-container
        (.on "load"
             (fn []
               (GET "./data/california-counties.json"
                    :response-format :json
                    :handler (partial draw-california quote-map-container)))))))
