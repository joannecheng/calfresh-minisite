(ns calfresh-minisite.quote-map
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljsjs.mapbox]
            [cljsjs.ScrollMagic]

            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]

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
(defn update-title [county-name]
  (let [el (.getElementById js/document "quote_county_name")]
    (set! (.-innerHTML el) (str "<h2>" county-name "</h2>"))))

(defn show-quotes [county-name]
  (let [county-quotes (get quotes/quotes county-name)
        el (.getElementById js/document "quote_list")]
    (set! (.-innerHTML el) "")

    (if (> (count county-quotes) 0)
      (set! (.-innerHTML el) (str "<blockquote class=\"animated fadeInDown\">"
                                  (rand-nth county-quotes)
                                  "</blockquote>"
                                  "<div class=\"profile-image\">Image here</div>")))))


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
  [quote-map-container]
  (go (let [resp (<! (http/get "./data/california-counties.json"))]
        (-> quote-map-container
            (.addSource "counties", (clj->js {:type "geojson" :data (:body resp)})
             ))
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
            (.on "mousemove", "california-county-fill",
                 #(let [county-id (.-id (first (.-features %)))
                        county-name (.-name (.-properties (first (.-features %))))]
                    (if (some? @hovered-state-id)
                      (set-county-hover quote-map-container
                                        @hovered-state-id false))
                    (if (not= @hovered-state-id county-id)
                      (update-quotes county-name))

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
    ;;(scroll-handlers quote-map-container)
    (draw-california quote-map-container)))
