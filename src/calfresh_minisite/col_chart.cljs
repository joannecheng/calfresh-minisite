(ns calfresh-minisite.col-chart
  (:require [cljsjs.d3]
            [clojure.string :as string]
            [calfresh-minisite.col-data :as col-data]))

(def height 300)
(def container-id "col_viz")
(def top-margin 15)
(def bar-position 55)
(def categories ["Housing" "Medical" "Transportation" "Child Care"])

;; General helpers
(defn translate-str [x y]
  (str "translate(" x "," y ")"))

(defn housing-cost-for-area [area-name data-row]
  ;;(let [rent (->> col-data/rents-by-county
  ;;                (filter #(= (second %) area-name))
  ;;                first)]
  ;;  (merge data-row { "Housing" (* (first rent) 12)}))
  )

(defn create-svg [width]
  (-> js/d3
   (.select (str "#" container-id))
   (.append "svg")
   (.attr "width" width)
   (.attr "height" height)))

(defn bar-scale [width]
  (-> js/d3
   (.scaleLinear)
   (.domain #js [0 70000])
   (.range #js [0 (- width 140)])))

;; Stacked bar helpers
(defn draw-segment [svg scale bar-height data-row]
  (fn [key-name]
    (-> svg
        (.append "rect")
        (.attr "x" 0)
        (.attr "y" 0)
        (.attr "width" (scale (get data-row key-name)))
        (.attr "height" bar-height)
        (.classed (string/replace (string/lower-case key-name) " " "-") true)
    )))

(defn stack-length [key-names data-row]
  (->> key-names
   (map #(get data-row %))
   (reduce +)))

(defn draw-stack [svg scale data-row offset label]
  (let [bar-height 15
        top-offset (+ offset top-margin)
        segments-svg (-> svg
                         (.append "g")
                         (.classed "segments" true)
                         (.classed "stacked-bar" true)
                         (.attr "transform"
                                (translate-str bar-position top-offset)))
        segment-generator (draw-segment segments-svg scale bar-height data-row)]

    (segment-generator "Housing")
    (-> (segment-generator "Medical")
        (.attr
         "transform"
         (translate-str (scale (stack-length (take 1 categories) data-row)) 0)))

    (-> (segment-generator "Transportation")
        (.attr
         "transform"
         (translate-str
          (scale (stack-length (take 2 categories) data-row)) 0)))

    (-> (segment-generator "Child Care")
        (.attr
         "transform"
         (translate-str
          (scale (stack-length (take 3 categories) data-row)) 0)))

    (-> (segment-generator "Food")
        (.attr
         "transform"
         (translate-str
          (scale (stack-length categories data-row)) 0)))

    (-> segments-svg
        (.append "foreignObject")
        (.attr "width" bar-position - 5)
        (.attr "height" bar-height)
        (.attr "x" (- (- bar-position) 4))
        (.attr "y" 1)
        (.attr "class" "label")
        (.append "xhtml:div")
        (.attr "xmlns" "http://www.w3.org/1999/xhtml")
        (.text label))
    ))

;; Main draw functions
(defn clear []
  (-> js/d3
   (.selectAll (str "#" container-id " svg"))
   (.remove)))

(defn draw [width]
  (let [scale (bar-scale width)
        svg (create-svg width)
        bar-spacing 45]

    (draw-stack svg scale (nth col-data/col 1) 15 "All CA")
    (draw-stack svg
                scale
                (housing-cost-for-area "San Francisco, CA HUD Metro FMR Area" (nth col-data/col 1))
                (+ bar-spacing 15)
                "SF Area")

    (draw-stack svg
                scale
                (housing-cost-for-area "San Jose-Sunnyvale-Santa Clara, CA HUD Metro FMR Area" (nth col-data/col 1))
                (+ (* 2 bar-spacing) 15)
                "San Jose")))

(defn redraw [width]
  (clear)
  (draw width))
