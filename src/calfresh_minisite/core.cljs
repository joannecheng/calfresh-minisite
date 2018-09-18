(ns calfresh-minisite.core
  (:require [calfresh-minisite.col-chart :as col-chart]
            [calfresh-minisite.col-vs-income :as col-vs-income]
            [calfresh-minisite.quote-map :as quote-map]
            [calfresh-minisite.utils :as utils]

            [cljsjs.ScrollMagic]))

;; UI State
(def ui-state
  (atom {;; col-vs-income: changes state of the col vs income
         ;; visualization
         ;; options: negative, sf-counties, la-counties
         :income-view :median ;; median, lower-quartile
         :current-section nil
         }))

;; Scroll Handlers
(defn side-nav-handler [element-id controller]
  (let [el-height (utils/height-of element-id)]
    (-> (js/ScrollMagic.Scene.
         #js {:triggerElement (str "#" element-id) :duration el-height})
      (.setClassToggle (str "#" element-id "_nav") "active")
      (.addTo controller))))

(defn add-side-nav-handlers []
  (let [element-ids ["making_ends_meet" "better_jobs" "disability_illness" "cta"]
        controller (js/ScrollMagic.Controller.)]
    (doseq [element-id element-ids] (side-nav-handler element-id controller))))

;; Drawing visualizations
(defn redraw-chart [draw-function element-id] (draw-function @ui-state (utils/width-of element-id) element-id)) (defn create-resize-handler [element-id]
  (.addEventListener
   js/window
   "resize"
   (partial redraw-chart col-vs-income/redraw element-id)))

;; Main Functions
;; The function that calls all the draw functions
(defn draw-index []
  (let [col-vs-income "col_vs_income"]
    (add-side-nav-handlers)
    (col-chart/redraw (utils/width-of "col_viz"))
    (create-resize-handler col-vs-income)
    (col-vs-income/set-click-handlers ui-state)

    (col-vs-income/redraw @ui-state
                          (utils/width-of col-vs-income)
                          col-vs-income)

    (add-watch ui-state :redraw
               (fn [_key _atom old-state new-state]
                 (col-vs-income/redraw new-state
                                       (utils/width-of col-vs-income)
                                       col-vs-income)))))

(defn ^:export main []
    (if (some? (.getElementById js/document "quote_map"))
      (quote-map/draw)
      (draw-index)))

(defn on-js-reload []
  ;; remove all event handlers created in here
  (col-vs-income/unset-click-handlers)
  (main))
