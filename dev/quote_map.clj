(ns quote-map
  (:require [hiccup.core :refer [html]]))

(defn header []
  (str (html [:head
              [:meta {:name "viewport"
                      :content "width=device-width, initial-scale=1.0"}]
              [:meta {:http-equiv "X-UA-Compatible"
                      :content "ie=edge"}]
              [:title "Voices"]
              [:script {:src "https://use.typekit.net/lkd6vsz.js"}]
              [:script "try{Typekit.load({ async: true });}catch(e){}"]

              [:link {:rel "stylesheet"
                      :href "https://use.fontawesome.com/releases/v5.2.0/css/all.css"
                      :integrity "sha384-hWVjflwFxL6sNzntih27bfxkr27PmbbK/iSvJ+a4+0owXq79v+lsFkW54bOGbiDQ"
                      :crossorigin "anonymous"}]

              [:link {:href "https://api.tiles.mapbox.com/mapbox-gl-js/v0.48.0/mapbox-gl.css"
                      :rel "stylesheet"}]

              [:link {:href "css/main.css"
                      :type "text/css"
                      :rel "stylesheet"}]

              [:link {:href "css/animate.min.css"
                      :rel "stylesheet"
                      :type "text/css"}]])))

(defn page-title []
  (html [:h1 "Voices of the CalFresh Program"]))

(defn county-name []
  (html [:div {:class "grid-item width-one-whole end-row"}
         [:div {:id "quote_county_name"}]]))

(defn quote-list []
  (html [:div {:id "quote_list"}]))

(defn compiled-clojurescript []
  (html [:script {:src "js/compiled/calfresh_minisite.js"
                  :type "text/javascript"}]))

(defn main-script []
  (html [:script "mapboxgl.accessToken = 'pk.eyJ1Ijoiam9hbm5lY2hlbmciLCJhIjoiYV9YSTdaZyJ9.tOevZpArPItdszzQl_GLJQ'; calfresh_minisite.core.main();"]))

(defn scrollmagic-indicators []
  (html [:script {:src "https://cdnjs.cloudflare.com/ajax/libs/ScrollMagic/2.0.5/plugins/debug.addIndicators.js"
                  :type "text/javascript"}]))

(defn body []
  (html [:body
         [:div {:id "quote_map_container"}
          [:div {:id "quote_map_quotes"}
           [:div {:class "quote-map-section"}
            (page-title)

            [:div {:class "quote-map-content"}
             [:div {:class "grid-box"}
              (county-name)]
             (quote-list)]]]

          [:div {:id "quote_map"}]]

         (compiled-clojurescript)
         (main-script)
         (scrollmagic-indicators)
         ]))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Main Render
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn render []
  (str "<!DOCTYPE html>"
   (html [:html {:lang "en"}
          (header)
          (body)])))
