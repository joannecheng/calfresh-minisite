(ns calfresh-minisite.col-vs-income
  (:require [cljsjs/d3]
            [calfresh-minisite.col-data :as col-data]
            [calfresh-minisite.tooltips :as tooltips]
            [calfresh-minisite.utils :as utils]))

(def categories ["Housing" "Medical" "Transportation" "Child Care" "Food" "Other" "Annual taxes"])
(def line-padding 18)

(def income-data-index {:lower-quartile 1
                        :median 2})

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; General Helpers
(defn create-svg [width element-id]
  (-> js/d3
      (.select (str "#" element-id))
      (.append "svg")
      (.attr "width" width)))

(defn line-scale [min-val max-val width]
  (-> js/d3
      (.scaleLinear)
      (.domain #js [(- min-val 2000) max-val])
      (.range #js [0 width])))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Data transforming related helpers
(defn income-for [county-name index]
  (->> col-data/income-data
       (filter #(= (first %) county-name))
       first
       (#(nth % index))))

(defn col-for-county [county-name index]
  (let [county-row (->> col-data/col-counties
                        (filter #(= (first %) county-name))
                        first)]
    (nth county-row index)))

(defn income-col-difference
  ;; Return median income, cost of living, and median income - cost of living
  [income-index county-name]
  (let [cost (col-for-county county-name 2)
        median-income (income-for county-name income-index)]
    [median-income cost (- median-income cost)]))

(defn filter-negative-counties [col-counties]
  (->> col-counties
       (filter #(> 2000 (last (last %))))))

(defn filter-sf-area [col-counties]
  (let [sf-counties ["Alameda" "Contra Costa" "San Francisco"
                     "San Mateo" "Marin"]]
    (->> col-counties
         (filter
          (fn [item] (some #{(first item)} sf-counties))))))

(defn filter-la-area [col-counties]
  (let [counties ["Los Angeles" "Riverside" "San Bernardino"
                     "Ventura" "Orange"]]
    (->> col-counties
         (filter
          (fn [item] (some #{(first item)} counties))))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; SVG drawing functions
(defn draw-lines [svg col-counties lscale]
    (-> svg
        (.selectAll "line.col-line")
        (.data (clj->js col-counties))
        (.enter)
        (.append "line")
        (.attr "class" #(if (< (last (last %)) 1000) "below"))
        (.classed "col-line" true)
        (utils/attrs {:x1 #(lscale (first (second %)))
                      :x2 #(lscale (second (second %)))
                      :y1 (fn [_ i] (* i line-padding))
                      :y2 (fn [_ i] (* i line-padding))})))

(defn draw-marker [svg col-counties lscale marker-class]
  ;; Cost of living is second element in second el (sorry, future joanne)
  (-> svg
      (.selectAll (str "circle." marker-class))
      (.data (clj->js col-counties))
      (.enter)
      (.append "circle")
      (.classed marker-class true)
      (.classed "marker" true)
      (utils/attrs {:r 2
              :cy (fn [_ i] (* i line-padding))
              :cx #(lscale (if (= marker-class "col-marker")
                             (second (second %))
                             (first (second %))))}))

  (defn draw-label [svg col-counties]
    (-> svg
        (.selectAll "text.county-label")
        (.data (clj->js col-counties))
        (.enter)
        (.append "text")
        (.classed "county-label" true)
        (.text #(first %))
        (utils/attrs {:x 0
                :y (fn [_ i] (+ 1.5 (* i line-padding)))}))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Grid, Axis, Labels, etc
(defn draw-grid [svg width col-counties tmargin]
  (-> svg
      (.selectAll "rect.row")
      (.data (clj->js col-counties))
      (.enter)
      (.append "rect")
      (.attr "class" (fn [_ i] (if (even? i) "even" "odd")))
      (.classed "row" true)
      (.on "mouseover" #(tooltips/draw-tooltip %))
      (.on "mouseout" tooltips/remove-tooltip)
      (utils/attrs {:transform (utils/translate-str 0 (/ line-padding -2))
              :x 0
              :y (fn [_ i] (* i line-padding))
              :width width
              :height line-padding})))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Controls interaction
(defn income-view [ui-state]
  (get ui-state :income-view))

(defn income-controls [ui-state]
  (let [income-controls (.select js/d3 "#income_controls")]
    (-> income-controls
        (.selectAll "a")
        (.classed "active" false))

     (-> income-controls
         (.select (str "[data-name='" (name (income-view ui-state)) "']"))
         (.classed "active" true))))

(defn interaction-controls [ui-state]
  (income-controls ui-state))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Public Drawing Functions
(defn draw [ui-state width element-id]
  (let [svg (create-svg width element-id)
        counties (map first col-data/income-data)
        col-counties (->> (map (partial income-col-difference
                                        (get income-data-index (income-view ui-state)))
                               counties)
                          (map vector counties)
                          (sort-by #(last (last %))))

        filtered-counties (filter-negative-counties col-counties)
        lmargin 110
        rmargin 15
        tmargin 7
        max-num (->> col-counties
                     (map second)
                     flatten
                     (reduce max))
        min-num (->> col-counties
                     (map #(take 2 (second %)))
                     flatten
                     (reduce min))
        grid-svg (-> svg
                     (.append "g")
                     (.classed "grid" true)
                     (.attr "transform" (utils/translate-str 0 tmargin)))
        lines-svg (-> svg
                      (.append "g")
                      (.classed "col-lines" true)
                      (.attr "transform" (utils/translate-str lmargin tmargin)))
        lscale (line-scale min-num max-num (- width (+ lmargin rmargin)))]
    (-> svg
        (.attr "height" (* line-padding (count filtered-counties))))

    ;; Grid, Legend, Annotations
    (interaction-controls ui-state)
    (draw-grid grid-svg width filtered-counties tmargin)
    (draw-lines lines-svg filtered-counties lscale)
    (draw-marker lines-svg filtered-counties lscale "income-marker")
    (draw-marker lines-svg filtered-counties lscale "col-marker")
    (draw-label lines-svg filtered-counties)))

(defn clear []
  (-> js/d3
      (.selectAll "svg")
      .remove))

(defn redraw [ui-state width element-id]
  (clear)
  (draw ui-state width element-id))

(defn set-click-handlers [ui-state]
  (-> (.selectAll js/d3 "#income_controls a")
      (.on "click" #(this-as t
                      (swap!
                       ui-state
                       assoc :income-view (keyword (.-name (.-dataset t))))))))

(defn unset-click-handlers []
  (-> (.selectAll js/d3 "#income_controls a")
      (.on "click" nil)))
