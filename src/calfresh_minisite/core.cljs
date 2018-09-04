(ns calfresh-minisite.core
    (:require [calfresh-minisite.col-chart :as col-chart]))

;; define your app data so that it doesn't get over-written on reload

(defn ^:export main []
  (col-chart/redraw))

(defn on-js-reload []
  (main))
