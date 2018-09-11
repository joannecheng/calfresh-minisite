(ns calfresh-minisite.core
  (:require [calfresh-minisite.col-chart :as col-chart]
            [calfresh-minisite.col-vs-income :as col-vs-income]
            [calfresh-minisite.quote-map :as quote-map]

            [cljsjs.ScrollMagic]))

;; UI State
(def ui-state
  (atom {;; col-vs-income: changes state of the col vs income
         ;; visualization
         ;; options: negative, sf-counties, la-counties
         :col-vs-income-view "negative"
         :current-section nil
         }))

;; Scroll Handlers
(defn height-of [element-id]
  (let [el (.getElementById js/document element-id)]
    (if (nil? el)
      0
      (.-clientHeight el))))

(defn side-nav-handler [element-id controller]
  (let [el-height (height-of element-id)]
    (-> (js/ScrollMagic.Scene.
         #js {:triggerElement (str "#" element-id) :duration el-height})
      (.setClassToggle (str "#" element-id "_nav") "active")
      (.addTo controller))))

(defn add-side-nav-handlers []
  (let [element-ids ["making_ends_meet" "better_jobs" "disability_illness"]
        controller (js/ScrollMagic.Controller.)]
    (doseq [element-id element-ids] (side-nav-handler element-id controller))))

;; Drawing visualizations
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
  (add-side-nav-handlers)
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
