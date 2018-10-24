(ns calfresh-minisite.ui-state-validators
  (:require [clojure.spec.alpha :as s]))

(s/def ::income-view #{:median :lower-quartile})
(s/def ::show-counties #{:show :hide})

(defn validate-ui-state [ui-state]
  (assert (s/valid? ::income-view (get ui-state :income-view)))
  (assert (s/valid? ::show-counties (get ui-state :show-counties))))

