(ns calfresh-minisite.rotating-quotes
  (:require [cljsjs.slick :as slick])
  )

(defn render []
  (println "render")
  (->
   (js/$ "#rotating_quotes_1")
   (.slick (clj->js {:setting-name "autoplay"})))
  )
