(ns calfresh-minisite.quote-view
  (:require [clojure.string :as string]
            [calfresh-minisite.quotes :as quotes]))


(defn random-quotes [quote-list]
  (case (count quote-list)
    0 ""
    1 quote-list
    (take 2 (shuffle quote-list))))

(defn title-contents [county-name]
  (str "<div class=\"large-text text-center\"><strong>"
       county-name
       "</strong></div>"))

(defn county-info-row [county-data row-name item-name]
  (let [item (get county-data item-name)]
    (str "<tr><td>"
         row-name
         "</td><td>"
         (str item "&nbsp;")
         "</td></tr>")))

(defn county-info-contents [county-name]
  (let [county-data (get quotes/quotes county-name)]
    (str "<table class=\"county-data\">"
         (county-info-row county-data "Population" :population)
         (county-info-row county-data "Min Cost of Living (2 Working Adults, 2 Children)"
                          :minimum-cost-living-family)
         (county-info-row county-data "Median Income" :median-income)
         (county-info-row county-data "Poverty Rate (Cost of Living Adujusted)" :poverty-rate)
         "</table>"
         )))

(defn quote-html [county-quote]
  (str "<blockquote class=\"animated fadeInDown\">"
       county-quote
       "</blockquote>"))

(defn preloaded-quotes [county-name]
  (let [county-quotes (random-quotes (get (get quotes/quotes county-name) :quotes))]
    (str "<div class=\"grid-box county-info-box\" data-county=\""
         county-name
         "\"/>"
         "<div class=\"grid-item shift-one-third width-one-half end-row\">"
         (title-contents county-name)
         "</div>"
         "<div class=\"grid-item shift-one-third width-one-half end-row\">"
         (county-info-contents county-name)
         (string/join " " (map quote-html county-quotes))
         "</div></div>")))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; DOM updating functions
(defn update-title [county-name]
  (let [el (.getElementById js/document "quote_county_name")]
    (set! (.-innerHTML el) (title-contents county-name))))

(defn show-quotes [county-name]
  (let [county-quotes (random-quotes (get (get quotes/quotes county-name) :quotes))
        el (.getElementById js/document "quote_list")
        county-info (county-info-contents county-name)]

    (set! (.-innerHTML el) (str county-info
                                (string/join " " (map quote-html (random-quotes county-quotes)))))
      ))

(defn preload-quotes [county-names]
  (let [el (.getElementById js/document "preloaded_quotes")]
    (set! (.-innerHTML el) (string/join "" (map preloaded-quotes county-names)))))
