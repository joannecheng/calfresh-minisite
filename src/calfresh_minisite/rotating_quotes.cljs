(ns calfresh-minisite.rotating-quotes
  (:require [cljsjs.slick :as slick]))

(defn get-pause-status [pause-button]
  (let [current-pause-status (.data pause-button "status")]
    (if (= current-pause-status "slickPlay")
      "slickPause"
      "slickPlay")))

(defn set-icon [pause-button pause-status]
  (if (= pause-status "slickPlay")
    (.addClass pause-button "playing")
    (.removeClass pause-button "playing")))

(defn set-pause-status [pause-button quote-list]
  (let [pause-status (get-pause-status pause-button)]
    (.data pause-button "status" pause-status) ;; set data attribute
    (.slick quote-list pause-status) ;; set slick element
    (set-icon pause-button pause-status))) ;; set text of button

(defn set-pause-button [rotating-quote-el]
  (let [pause-button (.find (js/$ rotating-quote-el) "a.pause-button")
        quote-list (.find (js/$ rotating-quote-el) "div.quote-list")]
    (.click pause-button
              (partial set-pause-status pause-button quote-list))))

(defn set-pause-buttons []
  (let [rotating-quotes-list (.toArray (js/$ ".rotating-quotes"))]
    (doseq [rotating-quote-el rotating-quotes-list]
      (set-pause-button rotating-quote-el))))

(defn render []
  (set-pause-buttons)
  (-> ".rotating-quotes .quote-list"
   js/$
   (.slick (clj->js {:autoplay true
                     :autoplaySpeed 4000}))))
