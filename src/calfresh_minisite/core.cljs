(ns calfresh-minisite.core
  (:require [calfresh-minisite.col-chart :as col-chart]
            [calfresh-minisite.col-vs-income :as col-vs-income]
            [calfresh-minisite.quote-map :as quote-map]

            [cljsjs.waypoints]))

;; UI State
(def ui-state
  (atom {;; col-vs-income: changes state of the col vs income
         ;; visualization
         ;; options: negative, sf-counties, la-counties
         :col-vs-income-view "negative"
         :current-section nil
         }))

;; Waypoints Handlers
(defn change-nav [element-id]
  (println "change nav"))

(defn side-nav-handler []
  (let [element-id "making_ends_meet"]
    (js/Waypoint.
     #js {:element (.getElementById js/document element-id)
          :handler (partial change-nav element-id)
          })
    ))

;; Cost of living visualization
(defn width-of [element-id]
  (let [el (.getElementById js/document element-id)]
    (if (nil? el)
      0
      (.-clientWidth el))))

(defn redraw-chart [draw-function element-id]
  (draw-function ui-state (width-of element-id) element-id))

(defn create-resize-handler [element-id]
  (.addEventListener
   js/window
   "resize"
   (partial redraw-chart col-vs-income/redraw element-id))
  )

;; Main Function
;; The function that calls all the draw functions
(defn ^:export main []
  (side-nav-handler)
  (let [col-vs-income "col_vs_income"]
    (col-chart/redraw (width-of "col_viz"))

    (create-resize-handler col-vs-income)
    (col-vs-income/redraw
     ui-state
     (width-of col-vs-income)
     col-vs-income)

    (if (some? (.getElementById js/document "quote_map"))
      (quote-map/draw))
    ))


(defn on-js-reload []
  ;; remove all event handlers created in here
  (main))
