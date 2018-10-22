(ns calfresh-minisite.rotating-quotes
  (:require [cljsjs.slick :as slick])
  )

(defn render []
  (->
   (js/$ ".rotating-quotes")
   (.slick (clj->js {:setting-name "autoplay"
                     :autoplay true
                     :autoplaySpeed 4000}))))
