(ns user
  (:require
   [figwheel-sidecar.repl-api :as f]
   [clojure.java.io :as io]
   [clojure.data.json :as json]))

;; user is a namespace that the Clojure runtime looks for and
;; loads if its available

;; You can place helper functions in here. This is great for starting
;; and stopping your webserver and other development services

;; The definitions in here will be available if you run "lein repl" or launch a
;; Clojure repl some other way

;; You have to ensure that the libraries you :require are listed in your dependencies

;; Once you start down this path
;; you will probably want to look at
;; tools.namespace https://github.com/clojure/tools.namespace
;; and Component https://github.com/stuartsierra/component


(defn fig-start
  "This starts the figwheel server and watch based auto-compiler."
  []
  ;; this call will only work are long as your :cljsbuild and
  ;; :figwheel configurations are at the top level of your project.clj
  ;; and are not spread across different lein profiles

  ;; otherwise you can pass a configuration into start-figwheel! manually
  (f/start-figwheel!))

(defn fig-stop
  "Stop the figwheel server and watch based auto-compiler."
  []
  (f/stop-figwheel!))

;; if you are in an nREPL environment you will need to make sure you
;; have setup piggieback for this to work
(defn cljs-repl
  "Launch a ClojureScript REPL that is connected to your build and host environment."
  []
  (f/cljs-repl))

(def gcf-counties ["Alameda" "Butte" "Contra Costa" "Del Norte"
                   "El Dorado" "Fresno" "Glenn" "Humboldt"
                   "Marin" "Monterey" "Nevada" "Orange"
                   "Placer" "Sacramento" "San Diego" "San Francisco"
                   "San Luis Obispo" "San Mateo" "Santa Barbara" "Santa Clara"
                   "Santa Cruz" "Siskiyou" "Solano" "Sonoma"
                   "Tehama" "Tulare" "Ventura" "Yolo"])

(defn feature-gcf?
  [feature-properties]
  (let [county-name (get-in feature-properties ["name"])]
    (some? (some #{county-name} gcf-counties))))

(defn new-properties
  [feature-properties]
  (assoc feature-properties "is_gcf" (feature-gcf? feature-properties)))

(defn edit-california-json
  []
  (let [json-resource (-> "public/data/california-counties.json"
                          io/resource
                          slurp
                          json/read-str)
        features (get json-resource "features")
        new-features (map #(assoc % "properties" (new-properties (get % "properties")))
                          features)
        new-json-resource (assoc json-resource "features" new-features)]

    (spit
     "resources/public/data/california-counties.json"
     (json/write-str new-json-resource))))

;;(edit-california-json)
