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
(def selected-county-id (atom 38))
(def selected-county-name (atom "Los Angeles"))
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
;; Quotes Event Handlers
(defn update-quotes []
  (let [county-name @selected-county-name]
    (quote-view/update-title county-name)
    (quote-view/show-quotes county-name)))

(defn set-selected-county [container county-id selected-bool]
  (reset! selected-county-id county-id)
  (-> container
      (.setFeatureState #js {:source "counties" :id county-id}
                        #js {:selected selected-bool})))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Drawing
(defn draw-california
  ;; This is complicated because of Mapbox Gl's DSL
  ;; See https://www.mapbox.com/mapbox-gl-js/example/hover-styles/ for more
  ;; information on what's going on
  [quote-map-container resp]
  (let [popup (js/mapboxgl.Popup. #js {:closeButton false :closeOnClick false})]

    (-> quote-map-container
        (.addSource "counties", (clj->js {:type "geojson" :data resp})))
    (-> quote-map-container
        (.addLayer (clj->js {:id "california-county-fill"
                             :type "fill"
                             :source "counties"
                             :layout {}
                             :paint {:fill-color "#e25050"
                                     :fill-opacity ["case"
                                                    ["boolean" ["feature-state" "selected"] false]
                                                    0.6 0]}})))
    (set-selected-county quote-map-container
                         @selected-county-id true)

    ;; display tooltips
    (-> quote-map-container
        (.on "mousemove" "california-county-fill"
             (fn [e]
               (let [feature (first (.-features e))
                     county-name (.-name (.-properties feature))
                     html-str (str "<div>" county-name " County</div>")]
                 (-> popup
                     (.setHTML html-str)
                     (.setLngLat (.-lngLat e))
                     (.addTo quote-map-container))))))

    (-> quote-map-container
        (.on "mouseout" "california-counties"
             (fn [] (.remove popup))))

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
                (if (some? @selected-county-id)
                  (set-selected-county quote-map-container
                                       @selected-county-id false))
                (reset! selected-county-name county-name)
                (set-selected-county quote-map-container county-id true)
                (update-quotes))))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Scroll Event Handling
(defn scroll-handlers
  ;; TODO: Should zoom to county
  [quote-map])

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
    (quote-view/preload-quotes ["San Francisco" "Alameda" "Fresno"])
    (update-quotes)
    ;;(scroll-handlers quote-map-container)
    (-> quote-map-container
        (.on "load"
             (fn []
               (GET "./data/california-counties.json"
                    :response-format :json
                    :handler (partial draw-california quote-map-container)))))
  ))
