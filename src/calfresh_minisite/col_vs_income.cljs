(ns calfresh-minisite.col-vs-income
  (:require [cljsjs/d3]
            [calfresh-minisite.col-data :as col-data]
            [calfresh-minisite.tooltips :as tooltips]
            [calfresh-minisite.utils :as utils]))

(def categories ["Housing" "Medical" "Transportation" "Child Care" "Food" "Other" "Annual taxes"])
(def line-padding 16)

(def income-data-index {:lower-quartile 1
                        :median 2})
(def family-type-index {:one_adult_one_child 2
                        :one_adult_two_children 3
                        :one_adult_three_children 4
                        :two_adults_one_child 5
                        :two_adults_two_children 6
                        :two_adults_three_children 7})

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; General Helpers
(defn create-svg [width element-id]
  (-> js/d3
      (.select (str "#" element-id))
      (.append "svg")
      (.attr "width" width)))

(defn line-scale [width]
  (-> js/d3
      (.scaleLinear)
      (.domain #js [0 93854])
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
  [col-index income-index county-name]
  (let [cost (col-for-county county-name col-index)
        median-income (income-for county-name income-index)]
    [median-income cost (- median-income cost)]))

(defn filter-negative-counties [col-counties]
  (->> col-counties
       (filter #(> 2000 (last (last %))))))

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

(defn draw-marker
  ;; Cost of living is second element in second el (sorry, future joanne)
  [svg col-counties lscale marker-class]

  (let [x-function #(lscale (if (= marker-class "col-marker")
                              (second (second %))
                              (first (second %))))
        marker-height 3]
    (-> svg
        (.selectAll (str "circle." marker-class))
        (.data (clj->js col-counties))
        (.enter)
        (.append "line")
        (.classed marker-class true)
        (.classed "marker" true)
        (utils/attrs {:y2 (fn [_ i] (+ (* i line-padding) marker-height))
                      :y1(fn [_ i] (- (* i line-padding) marker-height))
                      :x1 x-function
                      :x2 x-function
                      }))))

(defn draw-label [svg col-counties]
  (let [number-format (.format js/d3 "$,.0f")
        most-populated col-data/most-populated-counties]
    (-> svg
        (.append "text")
        (.text "Cost of living/Income diff")
        (utils/attrs {:x 10 :y -13
                      :font-weight "bold"
                      :font-color "grey"
                      :font-size "0.75em"}))
    (-> svg
        (.append "text")
        (.text "County")
        (utils/attrs {:x 0 :y -13
                      :text-anchor "end"
                      :font-weight "bold"
                      :font-color "grey"
                      :font-size "0.75em"}))
    (-> svg
        (.selectAll "text.number-label")
        (.data (clj->js col-counties))
        (.enter)
        (.append "text")
        (.classed "number-label" true)
        (.text #(number-format (last (last %))))
        (utils/attrs {:x 10
                      :y (fn [_ i] (+ 1.5 (* i line-padding)))}))
    (-> svg
        (.selectAll "text.county-label")
        (.data (clj->js col-counties))
        (.enter)
        (.append "text")
        (.classed "county-label" true)
        ;;(.text #(let [county-name (first %)]
        ;;           (if (contains? most-populated county-name)
        ;;             county-name)))
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

(defn draw-gridlines [svg width height scale tmargin lmargin]
  (let [grid-container (-> svg
                           (.append "g")
                           (.classed "grid-lines" true)
                           (.attr "transform" (utils/translate-str lmargin -7)))
        grid-vals #js [20000 30000 40000 50000 60000 70000 80000 90000 100000]]
    (-> grid-container
        (.selectAll "grid-line")
        (.data grid-vals)
        (.enter)
        (.append "line")
        (.classed "grid-line" true)
        (utils/attrs {:x1 #(scale %)
                      :x2 #(scale %)
                      :y1 0
                      :y2 (- height tmargin)
                      :stroke "#aaa"
                      :stroke-width 0.5}))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Controls interaction
(defn income-view [ui-state]
  (get ui-state :income-view))

(defn family-type [ui-state]
  (let [num-children (get ui-state :number-children)
        num-adults (get ui-state :number-adults)
        key-name (str (name num-adults) "_" (name num-children))]
    (keyword key-name)))

(defn clear-controls [el]
  (-> el
      (.selectAll "a")
      (.classed "active" false)))

(defn set-active-control [el active-state-name]
  (-> el
      (.select (str "[data-name='" active-state-name "']"))
      (.classed "active" true))
  )

(defn income-controls [ui-state]
  (let [income-controls (.select js/d3 "#income_controls")]
    (clear-controls income-controls)
    (set-active-control income-controls (name (income-view ui-state)))))

(defn family-type-controls [ui-state]
  (let [num-children-controls (.select js/d3 "#num_children_controls")
        num-adults-controls (.select js/d3 "#num_adults_controls")]
    (clear-controls num-children-controls)
    (clear-controls num-adults-controls)

    (set-active-control num-children-controls (name (get ui-state :number-children)))
    (set-active-control num-adults-controls (name (get ui-state :number-adults)))))

(defn interaction-controls [ui-state]
  (income-controls ui-state)
  (family-type-controls ui-state))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Public Drawing Functions
(defn draw [ui-state width element-id]
  (let [svg (create-svg width element-id)
        counties (map first col-data/income-data)
        col-counties (->> (map (partial income-col-difference
                                        (get family-type-index (family-type ui-state))
                                        (get income-data-index (income-view ui-state)))
                               counties)
                          (map vector counties)
                          (sort-by #(last (last %))))

        filtered-counties (filter-negative-counties col-counties)
        lmargin 110
        rmargin 15
        tmargin 23
        grid-svg (-> svg
                     (.append "g")
                     (.classed "grid" true)
                     (.attr "transform" (utils/translate-str 0 tmargin)))
        lines-svg (-> svg
                      (.append "g")
                      (.classed "col-lines" true)
                      (.attr "transform" (utils/translate-str lmargin tmargin)))
        lscale (line-scale (- width (+ lmargin rmargin)))
        height (+ tmargin (* line-padding (count filtered-counties)))]

    (-> svg
        (.attr "height" height))

    ;; Grid, Legend, Annotations
    (interaction-controls ui-state)
    (draw-grid grid-svg width filtered-counties tmargin)
    (draw-gridlines grid-svg width height lscale tmargin lmargin)
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
