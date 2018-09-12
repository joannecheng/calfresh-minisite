(ns calfresh-minisite.utils)

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
