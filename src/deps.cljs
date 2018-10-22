{:foreign-libs
 [{:file "waypoints/noframework.waypoints.js"
   :file-min "waypoints/noframework.waypoints.min.js"
   :provides ["cljsjs.waypoints"]}
  {:file "ScrollMagic/ScrollMagic.js"
   :provides ["cljsjs.ScrollMagic"]}
  {:file "slick/slick/slick.js"
   :file-min "slick/slick/slick.min.js"
   :provides ["cljsjs.slick"]}]
 :externs ["waypoints.ext.js", "ScrollMagic.ext.js", "slick.ext.js"]}
