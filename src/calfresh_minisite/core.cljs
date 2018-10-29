(ns calfresh-minisite.core
  (:require ;;[calfresh-minisite.col-chart :as col-chart]
            [calfresh-minisite.col-vs-income :as col-vs-income]
            [calfresh-minisite.quote-map :as quote-map]
            [calfresh-minisite.utils :as utils]
            [calfresh-minisite.rotating-quotes :as rq]
            [calfresh-minisite.ui-state-validators :as v]

            [ajax.core :refer [GET]]
            [cljsjs.ScrollMagic]))

;; UI State
(def ui-state
  (atom {;; col-vs-income: changes state of the col vs income
         ;; visualization
         ;; options: negative, sf-counties, la-counties
         :income-view :median ;; median, lower-quartile
         :number-children :two_children
         :number-adults :one_adult
         :show-counties :hide
         :current-section nil}))

;; Scroll Handlers
(defn side-nav-handler [element-id controller]
  (let [el-height (utils/height-of element-id)]
    (-> (js/ScrollMagic.Scene.
         #js {:triggerElement (str "#" element-id) :duration el-height})
      (.setClassToggle (str "#" element-id "_nav") "active")
      (.addTo controller))))

(defn add-side-nav-handlers [controller]
  (let [element-ids ["making_ends_meet" "better_jobs" "disability_illness" "calfresh_results" "cta"]]
    (doseq [element-id element-ids] (side-nav-handler element-id controller))))

;;(defn add-col-handler [controller container-id width]
;;  (-> (js/ScrollMagic.Scene.
;;       #js {:triggerElement "#col_viz_humbolt" :duration 400})
;;      (.on "enter" (partial col-chart/draw-humbolt container-id width))
;;      (.addIndicators)
;;      (.addTo controller)))

;; Drawing visualizations
(defn redraw-chart [draw-function element-id] (draw-function @ui-state (utils/width-of element-id) element-id)) (defn create-resize-handler [element-id]
  (.addEventListener
   js/window
   "resize"
   (partial redraw-chart col-vs-income/redraw element-id)))

;; Main Functions
;; The function that calls all the draw functions
(defn draw-index []
  (let [col-vs-income "col_vs_income"
        col-viz       "col_viz"
        col-viz-width (utils/width-of col-viz)
        controller    (js/ScrollMagic.Controller.)]
    (add-side-nav-handlers controller)
    ;;(add-col-handler controller col-viz col-viz-width)
    ;;(col-chart/redraw col-viz col-viz-width)

    (create-resize-handler col-vs-income)
    (col-vs-income/set-click-handlers ui-state)
    (col-vs-income/redraw @ui-state
                          (utils/width-of col-vs-income)
                          col-vs-income)
    (rq/render)

    (add-watch ui-state :redraw
               (fn [_key _atom old-state new-state]
                 (v/validate-ui-state new-state)
                 (col-vs-income/redraw new-state
                                       (utils/width-of col-vs-income)
                                       col-vs-income)))))

(defn ^:export main []
  (GET "./data/quotes.json"
       :response-format :json
       :handler (partial quote-map/draw))
  (draw-index))

(defn on-js-reload []
  ;; remove all event handlers created in here
  (col-vs-income/unset-click-handlers)
  (main))
