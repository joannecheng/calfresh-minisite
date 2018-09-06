(ns calfresh-minisite.core
  (:require [calfresh-minisite.col-chart :as col-chart]
            [calfresh-minisite.col-vs-income :as col-vs-income]))

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
  (col-chart/redraw (width-of "col_viz")))

(defn ^:export col_main [])

(defn on-js-reload []
  ;; remove all event handlers created in here
  (main))
