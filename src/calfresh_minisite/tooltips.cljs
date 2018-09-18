(ns calfresh-minisite.tooltips
  (:require [cljsjs/d3]
            [calfresh-minisite.utils :as utils]))

(def tmargin 6)
(def lmargin 5)

(defn tooltip-svg []
  (-> (.select js/d3 "svg")
      (.append "g")
      (.classed "tooltip-container" true)))

(defn tooltip-background [tooltip-container]
  (-> tooltip-container
      (.append "rect")
      (.classed "tooltip-background" true)
      (utils/attrs {:x 0 :y 0
                    :width 150 :height 40})))

(defn tooltip-text [tooltip-container data-row]
  (let [title (first data-row)
        med-income (first (last data-row))
        col (second (last data-row))
        tooltip-text (-> tooltip-container
                         (.append "g")
                         (.classed "tooltip-text" true))]
  (-> tooltip-text
      (.append "text")
      (.classed "tooltip-title" true)
      (.text (str title " County"))
      (utils/attrs {:x (+ lmargin)
                    :y (+ tmargin 10) }))
  (-> tooltip-text
      (.append "text")
      (.classed "tooltip-description" true)
      (.text (str med-income "-" col))
      (utils/attrs {:x (+ lmargin)
                     :y (+ tmargin 20)}))

  ))

(defn position-tooltip [tooltip-container]
  (let [x (.-offsetX (.-event js/d3))
        y (.-offsetY (.-event js/d3))]
    (-> tooltip-container
        (.attr "transform" (utils/translate-str x y)))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Public methods

(defn draw-tooltip [data-row]
  (let [tooltip-container (tooltip-svg)]
    (tooltip-background tooltip-container)
    (tooltip-text tooltip-container data-row)
    (position-tooltip tooltip-container)
    ))

(defn remove-tooltip []
  (-> (.select js/d3 "g.tooltip-container")
      (.remove)))
