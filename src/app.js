import Sticky from "sticky-js"
import * as d3 from "d3";

import COLGraph from "./col-barchart"
import QuoteMap from "./quote-map"

import "./assets/scss/app.scss"
import "./assets/media/grocery.jpg"

// Initialize Sticky side-nav
new Sticky("#side_nav")


// *********** cost of living *************
const colData = [
  {"Type":"1 Adult","Food":3564,"Child Care":0,"Medical":2190,"Housing":12551,"Transportation":3930,"Other":2855,"Required annual income after taxes":25090,"Annual taxes":4043,"Required annual income before taxes":29133},
  {"Type":"1 Adult, 1 Child","Food":5245,"Child Care":8421,"Medical":6952,"Housing":18827,"Transportation":8121,"Other":4616,"Required annual income after taxes":52183,"Annual taxes":9562,"Required annual income before taxes":61744},
  {"Type":"1 Adult, 2 Children","Food":7893,"Child Care":14182,"Medical":6666,"Housing":18827,"Transportation":8526,"Other":5091,"Required annual income after taxes":61185,"Annual taxes":11649,"Required annual income before taxes":72834},
  {"Type":"1 A 3 C","Food":10476,"Child Care":19943,"Medical":6725,"Housing":26089,"Transportation":10235,"Other":6512,"Required annual income after taxes":79979,"Annual taxes":16019,"Required annual income before taxes":95998},
  {"Type":"2 Adult (1 working)","Food":6533,"Child Care":0,"Medical":5233,"Housing":14768,"Transportation":8121,"Other":4616,"Required annual income after taxes":39272,"Annual taxes":6775,"Required annual income before taxes":46046},
  {"Type":"2 A (1 w) 1 C","Food":8124,"Child Care":0,"Medical":6666,"Housing":18827,"Transportation":8526,"Other":5091,"Required annual income after taxes":47235,"Annual taxes":8475,"Required annual income before taxes":55710},
  {"Type":"2 A (1 w) 2 C","Food":10487,"Child Care":0,"Medical":6725,"Housing":18827,"Transportation":10235,"Other":6512,"Required annual income after taxes":52786,"Annual taxes":9696,"Required annual income before taxes":62483},
  {"Type":"2 A (1 w) 3C","Food":12773,"Child Care":0,"Medical":6389,"Housing":26089,"Transportation":10196,"Other":6041,"Required annual income after taxes":61486,"Annual taxes":11719,"Required annual income before taxes":73206},
  {"Type":"2 A","Food":6533,"Child Care":0,"Medical":5233,"Housing":14768,"Transportation":8121,"Other":4616,"Required annual income after taxes":39272,"Annual taxes":6775,"Required annual income before taxes":46046},
  {"Type":"2 A 1 C","Food":8124,"Child Care":8421,"Medical":6666,"Housing":18827,"Transportation":8526,"Other":5091,"Required annual income after taxes":55656,"Annual taxes":10363,"Required annual income before taxes":66019},
  {"Type":"2A 2 C","Food":10487,"Child Care":14182,"Medical":6725,"Housing":18827,"Transportation":10235,"Other":6512,"Required annual income after taxes":66968,"Annual taxes":12994,"Required annual income before taxes":79962},
  {"Type":"2A 3C","Food":12773,"Child Care":19943,"Medical":6389,"Housing":26089,"Transportation":10196,"Other":6041,"Required annual income after taxes":81429,"Annual taxes":16356,"Required annual income before taxes":97785}]

const rentsByCounty = [["rent50_1","areaname18"],
  [2000,"Oakland-Fremont, CA HUD Metro FMR Area"],
  [764,"Alpine County, CA"],
  [829,"Amador County, CA"],
  [834,"Chico, CA MSA"],
  [822,"Calaveras County, CA"],
  [780,"Colusa County, CA"],
  [759,"Del Norte County, CA"],
  [916,"Sacramento--Roseville--Arden-Arcade, CA HUD Metro FMR Area"],
  [819,"Fresno, CA MSA"],
  [655,"Glenn County, CA"],
  [780,"Humboldt County, CA"],
  [723,"El Centro, CA MSA"],
  [805,"Inyo County, CA"],
  [744,"Bakersfield, CA MSA"],
  [810,"Hanford-Corcoran, CA MSA"],
  [762,"Lake County, CA"],
  [699,"Lassen County, CA"],
  [1388,"Los Angeles-Long Beach-Glendale, CA HUD Metro FMR Area"],
  [768,"Madera, CA MSA"],
  [777,"Mariposa County, CA"],
  [826,"Mendocino County, CA"],
  [660,"Merced, CA MSA"],
  [533,"Modoc County, CA"],
  [988,"Mono County, CA"],
  [1227,"Salinas, CA MSA"],
  [1303,"Napa, CA MSA"],
  [977,"Nevada County, CA"],
  [1610,"Santa Ana-Anaheim-Irvine, CA HUD Metro FMR Area"],
  [916,"Sacramento--Roseville--Arden-Arcade, CA HUD Metro FMR Area"],
  [798,"Plumas County, CA"],
  [1003,"Riverside-San Bernardino-Ontario, CA MSA"],
  [916,"Sacramento--Roseville--Arden-Arcade, CA HUD Metro FMR Area"],
  [1389,"San Benito County, CA HUD Metro FMR Area"],
  [1003,"Riverside-San Bernardino-Ontario, CA MSA"],
  [1400,"San Diego-Carlsbad, CA MSA"],
  [2704,"San Francisco, CA HUD Metro FMR Area"],
  [790,"Stockton-Lodi, CA MSA"],
  [1176,"San Luis Obispo-Paso Robles-Arroyo Grande, CA MSA"],
  [1725,"Santa Maria-Santa Barbara, CA MSA"],
  [2217,"San Jose-Sunnyvale-Santa Clara, CA HUD Metro FMR Area"],
  [1589,"Santa Cruz-Watsonville, CA MSA"],
  [759,"Redding, CA MSA"],
  [957,"Sierra County, CA"],
  [666,"Siskiyou County, CA"],
  [1145,"Vallejo-Fairfield, CA MSA"],
  [1511,"Santa Rosa, CA MSA"],
  [855,"Modesto, CA MSA"],
  [720,"Yuba City, CA MSA"],
  [646,"Tehama County, CA"],
  [695,"Trinity County, CA"],
  [678,"Visalia-Porterville, CA MSA"],
  [795,"Tuolumne County, CA"],
  [1399,"Oxnard-Thousand Oaks-Ventura, CA MSA"],
  [978,"Yolo, CA HUD Metro FMR Area"],
  [720,"Yuba City, CA MSA"]]


const col = "cost_of_living"
const colViz = "col_viz"
const height = 300
const colors = ["#bd1b3c", "#8c6651", "#5d8278", "#3591c0"]

if (document.getElementById(col)) {
  let width = document.getElementById(colViz).clientWidth
  let colGraph = new COLGraph(height, width, "#" + colViz)
  colGraph.createSVG()

  drawCOL(colGraph)

  window.addEventListener('resize', function() {
    width = document.getElementById(colViz).clientWidth

    colGraph.removeAll()
    colGraph.width = width
    colGraph.createSVG()
    drawCOL(colGraph)
  });
}

if (document.getElementById("quote_map")) {
  const quoteMap = new QuoteMap();
}

function housingCostForArea(areaName) {
  const rent = rentsByCounty.find(function(element) {
    return element[1] == areaName
  })[0]
  return { Housing: rent * 12 }
}

function drawCOL(colGraph) {
  const barSpacing = 45;
  // 1 adult, 1 child, San Fran
  colGraph.drawStack(colData[1], 15, "All CA")
  colGraph.drawStack(Object.assign(colData[1], housingCostForArea("San Francisco, CA HUD Metro FMR Area")),
                     barSpacing+15, "San Fran")
  colGraph.drawStack(Object.assign(colData[1], housingCostForArea("Los Angeles-Long Beach-Glendale, CA HUD Metro FMR Area")),
                     barSpacing*2+15, "LA")
  colGraph.drawStack(
    Object.assign(colData[1], housingCostForArea("San Jose-Sunnyvale-Santa Clara, CA HUD Metro FMR Area")),
                     barSpacing*3+15, "San Jose")
  
  colGraph.drawMedianIncome()
  colGraph.drawLegend()
}
