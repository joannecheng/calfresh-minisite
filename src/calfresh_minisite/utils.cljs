(ns calfresh-minisite.utils
  (:require [cljsjs.d3 :as d3]))

(defn height-of [element-id]
  (let [el (.getElementById js/document element-id)]
    (if (nil? el)
      0
      (.-clientHeight el))))

(defn width-of [element-id]
  (let [el (.getElementById js/document element-id)]
    (if (nil? el)
      0
      (.-clientWidth el))))

(defn attrs [el m]
  (doseq [[k v] m]
    (.attr el k v)))

(defn translate-str [x y]
  (str "translate(" x "," y ")"))

(defn format-money [num]
  (let [formatter (.format js/d3 "($,.0f")]
    (formatter num)))
