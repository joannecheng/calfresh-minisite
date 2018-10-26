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
(def selected-county-name (atom nil))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Quotes Event Handlers
(defn update-quotes []
  (let [county-name @selected-county-name]
    (quote-view/update-title county-name)
    (quote-view/show-quotes county-name)))

(defn set-selected-county [container county-id]
  (-> container
      (.setFeatureState #js {:source "counties" :id @selected-county-id}
                        #js {:selected false}))
  (reset! selected-county-id county-id)
  (-> container
      (.setFeatureState #js {:source "counties" :id @selected-county-id}
                        #js {:selected true})))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Map Panning Actions
(defn find-county-id-from-name [features county-name]
  (let [matching (->> features
                      js->clj
                      (filter #(= county-name (get-in % ["properties" "name"])))
                      first)]
    (get matching "id")))

(defn title-action [quote-map]
  (let [features (.-features (.-_data (.getSource quote-map "counties")))]
    (if (some? @selected-county-name)
      (find-county-id-from-name features @selected-county-name)
      (set-selected-county quote-map nil))
    (.easeTo quote-map (clj->js {:center [-119.4179 36.772537]
                                 :zoom   2}))))

(defn ease-to-county [quote-map county-name county-id]
  (let [center (get (get quotes/quotes county-name) :center)]
    (set-selected-county quote-map county-id)
    (.easeTo quote-map (clj->js {:center center
                                 :zoom   7}))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Scroll Event Handling
(defn scroll-handler-for-new-county
  [quote-map-container controller]
  (->  {:triggerElement "#user_selected_county"}
       clj->js
       (js/ScrollMagic.Scene.)
       (.on "enter" (partial title-action quote-map-container))
       ;;(.addIndicators)
       (.addTo controller)))

(defn scroll-handler-for-preloaded-county
  [quote-map-container controller county-name county-id]
  (->  {:triggerElement (str "[data-county=\"" county-name "\"]") :duration 500}
       clj->js
       (js/ScrollMagic.Scene.)
       (.on "enter end" (partial ease-to-county quote-map-container county-name county-id))
       ;;(.addIndicators)
       (.addTo controller)))

(defn scroll-handlers
  ;; TODO: Should zoom to county
  [quote-map-container]
  (let [controller (js/ScrollMagic.Controller. #js {})]
    (scroll-handler-for-new-county quote-map-container controller)
    (scroll-handler-for-preloaded-county quote-map-container
                                            controller
                                            "San Francisco" 38)
    (scroll-handler-for-preloaded-county quote-map-container
                                         controller
                                         "Alameda" 1)))


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
        (.addLayer (clj->js {:id "is-gcf-fill"
                             :type "fill"
                             :source "counties"
                             :layout {}
                             :paint {:fill-color "#999"
                                     :fill-opacity ["case"
                                                    ["boolean" ["get" "is_gcf"] false]
                                                    1 0.2]}})))

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
                         @selected-county-id)

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
                    county-name (.-name (.-properties (first (.-features %))))
                    top (.-offsetTop (.getElementById js/document "user_selected_county"))]

                (.scrollTo js/window 0 top)
                (reset! selected-county-name county-name)
                (ease-to-county quote-map-container county-name county-id)
                (update-quotes))))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Main Drawing functions
(defn draw-map []
  (js/mapboxgl.Map.
   #js {:container "quote_map"
        :style     "mapbox://styles/mapbox/dark-v9",
        :maxBounds (js/mapboxgl.LngLatBounds.
                    #js [-124.48200988, 31.52952194]
                    #js [-114.13077545, 43.00950241])}))

(defn draw []
  (let [quote-map-container (draw-map)]
    (quote-view/preload-quotes ["San Francisco" "Alameda"])
    (update-quotes)
    (scroll-handlers quote-map-container)
    (-> quote-map-container
        (.on "load"
             (fn []
               (GET "./data/california-counties.json"
                    :response-format :json
                    :handler (partial draw-california quote-map-container)))))))
