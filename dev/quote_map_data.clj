(ns quote-map-data
  (:require [hickory.core :as hickory]
            [hickory.select :as s]
            [clojure.string :as string]))

;; Get population, latitude, longitude
;; https://en.wikipedia.org/wiki/User:Michael_J/County_table


(def resp (slurp "https://en.wikipedia.org/wiki/User:Michael_J/County_table"))
(def parsed-doc (hickory/parse resp))
(def content (hickory/as-hickory parsed-doc))

(defn get-content [item]
  (-> item
      (get :content)
      first
      (get :content)
      first))

(defn get-single-content [item]
  (-> item
      (get :content)
      first))

(defn coord-str->float [coord-str]
  (-> (subs coord-str 0 (- (count coord-str) 2))
      read-string))

(defn merge-existing-row [new-row]
  (let [k (key new-row)]
    (merge (get quotes k) (get new-row k))))

(defn county-data [row]
  (let [tds          (s/select (s/tag "td") row)
        name         (get-content (nth tds 3))
        population   (get-single-content (nth tds 5))
        lat          (coord-str->float (get-single-content (nth tds 12)))
        lng          (coord-str->float (get-single-content (nth tds 13)))
        existing-row (get quotes name)]

    {name (merge existing-row {:population population
                               :center     [lng lat]})}))

(defn all-county-data [rows]
  (apply merge (map county-data rows)))

;;(def rows (s/select (s/tag "tr") content))
;;(all-county-data (subvec rows 187 245))
