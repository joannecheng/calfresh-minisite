(ns calfresh-minisite.quote-map
  (:require [cljsjs.mapbox]
            [cljsjs.waypoints]))

(defn waypoints-handlers []
  (js/Waypoint.
   #js {:element (.getElementById js/document "section1")
        :handler (fn [] (println "ok"))
        })
  )

(defn draw-map []
  (let [quote-map-container (js/mapboxgl.Map.
                             #js {
                                  :container "quote_map"
                                  :style "mapbox://styles/mapbox/dark-v9",
                                  :maxBounds (js/mapboxgl.LngLatBounds.
                                              #js [-124.48200988, 32.52952194]
                                              #js [-114.13077545, 42.00950241])
                                  })
        ])
  )

(defn draw []
  (draw-map)
  (waypoints-handlers))
