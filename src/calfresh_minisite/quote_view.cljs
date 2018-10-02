(ns calfresh-minisite.quote-view
  (:require [clojure.string :as string]
            [calfresh-minisite.quotes :as quotes]))

;;TODO USE HICCUP GODDAMMIT THIS IS SO BAD
(defn update-title [county-name]
  (let [el (.getElementById js/document "quote_county_name")]
    (set! (.-innerHTML el) (str "<div class=\"large-text\"><strong>"
                                county-name
                                "</strong></div>"))))

(defn county-info-row [county-data row-name item-name]
  (let [item (get county-data item-name)]
    (str "<div class=\"grid-item width-two-thirds county-data\">"
         row-name
         "</div><div class=\"grid-item width-one-third county-data end-row\">"
         (str item "&nbsp;")
         "</div>")))

(defn county-info [county-name]
  (let [county-data (get quotes/quotes county-name)]
    (str "<div class=\"grid-box county-info\">"
         (county-info-row county-data "Population" :population)
         (county-info-row county-data "Min Cost of Living (2 Working Adults, 2 Children)"
                          :minimum-cost-living-family)
         (county-info-row county-data "Median Income" :median-income)
         (county-info-row county-data "Poverty Rate (Cost of Living Adujusted)" :poverty-rate)
         "</div>"
         )))

(defn quote-html [county-quote]
  (str "<blockquote class=\"animated fadeInDown\">"
       county-quote
       "</blockquote>"))

(defn show-quotes [county-name]
  (let [county-quotes (get (get quotes/quotes county-name) :quotes)
        el (.getElementById js/document "quote_list")]
    (set! (.-innerHTML el) "")

    (if (> (count county-quotes) 0)
      (set! (.-innerHTML el) (str (county-info county-name)
                                  (string/join " " (map quote-html county-quotes)))))))

