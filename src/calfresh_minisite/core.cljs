(ns calfresh-minisite.core
  (:require [calfresh-minisite.col-chart :as col-chart]
            [calfresh-minisite.col-vs-income :as col-vs-income]
            [calfresh-minisite.quote-map :as quote-map]))

;; Cost of living visualization
(defn width-of [element-id]
  (let [el (.getElementById js/document element-id)]
    (if (nil? el)
      0
      (.-clientWidth el))))

(defn redraw-chart []
  (col-chart/redraw (width-of "col_viz")))

(defn create-resize-handler []
  (.addEventListener js/window "resize" redraw-chart))

;; Main Functions
(defn ^:export main []
  (create-resize-handler)
  (col-vs-income/redraw (width-of "col_vs_income"))
  (col-chart/redraw (width-of "col_viz"))

  (if (some? (.getElementById js/document "quote_map"))
    (quote-map/draw))
  )


(defn on-js-reload []
  ;; remove all event handlers created in here
  (main))
