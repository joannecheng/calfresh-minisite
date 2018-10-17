(ns calfresh-minisite.col-chart
  (:require [cljsjs.d3]
            [clojure.string :as string]
            [calfresh-minisite.col-data :as col-data]
            [calfresh-minisite.utils :as utils]))

(def height 300)
(def top-margin 15)
(def bar-position 55)
(def categories ["Housing" "Medical" "Transportation" "Child Care"])

;; General helpers
(defn housing-cost-for-area [area-name data-row]
  ;;(let [rent (->> col-data/rents-by-county
  ;;                (filter #(= (second %) area-name))
  ;;                first)]
  ;;  (merge data-row { "Housing" (* (first rent) 12)}))
  )

(defn create-svg [container-id width]
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
  (let [bar-height        15
        top-offset        (+ offset top-margin)
        segments-svg      (-> svg
                              (.append "g")
                              (.classed "segments" true)
                              (.classed "stacked-bar" true)
                              (.attr "transform"
                                     (utils/translate-str bar-position top-offset)))
        segment-generator (draw-segment segments-svg scale bar-height data-row)]

    (segment-generator "Housing")
    (-> (segment-generator "Medical")
        (.attr
         "transform"
         (utils/translate-str (scale (stack-length (take 1 categories) data-row)) 0)))

    (-> (segment-generator "Transportation")
        (.attr
         "transform"
         (utils/translate-str
          (scale (stack-length (take 2 categories) data-row)) 0)))

    (-> (segment-generator "Child Care")
        (.attr
         "transform"
         (utils/translate-str
          (scale (stack-length (take 3 categories) data-row)) 0)))

    (-> (segment-generator "Food")
        (.attr
         "transform"
         (utils/translate-str
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
        (.text label))))

(defn draw-legend [svg width]
  (let [labels #js ["Housing" "Medical" "Transportation" "Child Care" "Food"]
        box-size 15
        position (utils/translate-str (- width 20) top-margin)
        legend-container (-> svg (.append "g")
                             (.classed "legend" true)
                             (.attr "transform" position))]
    (-> legend-container
        (.selectAll "rect")
        (.data labels)
        (.enter)
        (.append "rect")
        (utils/attrs {:width box-size
                      :height box-size
                      :x 0
                      :y (fn [_ i] (* i 25))
                      :class #(string/replace (string/lower-case %) " " "-")}))
    (-> legend-container
        (.selectAll "text")
        (.data labels)
        (.enter)
        (.append "text")
        (.text #(str %))
        (utils/attrs {:x -5
                      :y (fn [_ i] (+ (* i 25) (* box-size 0.75)))}))))

(defn draw-title [container-id category]
  (-> js/d3
      (.select (str "#" container-id " h3"))
      (.text (str "Cost of Living Breakdown, " category))))

;; Main draw functions
(defn clear [container-id]
  (-> js/d3
   (.selectAll (str "#" container-id " svg"))
   (.remove)))

(defn draw-humbolt [container-id width]
  (clear container-id)
  (let [scale       (bar-scale width)
        svg         (create-svg container-id width)
        bar-spacing 75]
    (draw-title container-id "Humbolt County")

    (draw-stack svg scale (nth col-data/col 1) 0 "1 Working Adult, 1 Child")
    (draw-stack svg scale (nth col-data/col 2) bar-spacing "1 Working Adult, 2 Children")
    (draw-stack svg scale (nth col-data/col 10) (* 2 bar-spacing) "2 Working Adults, 2 Children")
    (draw-legend svg width)))

(defn draw [container-id width]
  (let [scale       (bar-scale width)
        svg         (create-svg container-id width)
        bar-spacing 75]

    (draw-title container-id "All California")
    (draw-stack svg scale (nth col-data/col 1) 0 "1 Working Adult, 1 Child")
    (draw-stack svg scale (nth col-data/col 2) bar-spacing "1 Working Adult, 2 Children")
    (draw-stack svg scale (nth col-data/col 10) (* 2 bar-spacing) "2 Working Adults, 2 Children")
    (draw-legend svg width)
    ;; Draw for counties with many applicants

    ;;(draw-stack svg
    ;;            scale
    ;;            (housing-cost-for-area "San Jose-Sunnyvale-Santa Clara, CA HUD Metro FMR Area" (nth col-data/col 1))
    ;;            (+ (* 2 bar-spacing) 15)
    ;;            "San Jose")
    ))

(defn redraw [container-id width]
  (clear container-id)
  (draw container-id width))
