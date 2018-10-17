(ns calfresh-minisite.col-data)

(def col
  [{"Type" "1 Adult","Food" 3564,"Child Care" 0,"Medical" 2190,"Housing" 12551,"Transportation" 3930,"Other" 2855,"Required annual income after taxes" 25090,"Annual taxes" 4043,"Required annual income before taxes" 29133}
   {"Type" "1 Adult, 1 Child","Food" 5245,"Child Care" 8421,"Medical" 6952,"Housing" 18827,"Transportation" 8121,"Other" 4616,"Required annual income after taxes" 52183,"Annual taxes" 9562,"Required annual income before taxes" 61744}
   {"Type" "1 Adult, 2 Children","Food" 7893,"Child Care" 14182,"Medical" 6666,"Housing" 18827,"Transportation" 8526,"Other" 5091,"Required annual income after taxes" 61185,"Annual taxes" 11649,"Required annual income before taxes" 72834}
   {"Type" "1 A 3 C","Food" 10476,"Child Care" 19943,"Medical" 6725,"Housing" 26089,"Transportation" 10235,"Other" 6512,"Required annual income after taxes" 79979,"Annual taxes" 16019,"Required annual income before taxes" 95998}
   {"Type" "2 Adult (1 working)","Food" 6533,"Child Care" 0,"Medical" 5233,"Housing" 14768,"Transportation" 8121,"Other" 4616,"Required annual income after taxes" 39272,"Annual taxes" 6775,"Required annual income before taxes" 46046}
   {"Type" "2 A (1 w) 1 C","Food" 8124,"Child Care" 0,"Medical" 6666,"Housing" 18827,"Transportation" 8526,"Other" 5091,"Required annual income after taxes" 47235,"Annual taxes" 8475,"Required annual income before taxes" 55710}
   {"Type" "2 A (1 w) 2 C","Food" 10487,"Child Care" 0,"Medical" 6725,"Housing" 18827,"Transportation" 10235,"Other" 6512,"Required annual income after taxes" 52786,"Annual taxes" 9696,"Required annual income before taxes" 62483}
   {"Type" "2 A (1 w) 3C","Food" 12773,"Child Care" 0,"Medical" 6389,"Housing" 26089,"Transportation" 10196,"Other" 6041,"Required annual income after taxes" 61486,"Annual taxes" 11719,"Required annual income before taxes" 73206}
   {"Type" "2 A","Food" 6533,"Child Care" 0,"Medical" 5233,"Housing" 14768,"Transportation" 8121,"Other" 4616,"Required annual income after taxes" 39272,"Annual taxes" 6775,"Required annual income before taxes" 46046}
   {"Type" "2 A 1 C","Food" 8124,"Child Care" 8421,"Medical" 6666,"Housing" 18827,"Transportation" 8526,"Other" 5091,"Required annual income after taxes" 55656,"Annual taxes" 10363,"Required annual income before taxes" 66019}
   {"Type" "2A 2 C","Food" 10487,"Child Care" 14182,"Medical" 6725,"Housing" 18827,"Transportation" 10235,"Other" 6512,"Required annual income after taxes" 66968,"Annual taxes" 12994,"Required annual income before taxes" 79962}
   {"Type" "2A 3C","Food" 12773,"Child Care" 19943,"Medical" 6389,"Housing" 26089,"Transportation" 10196,"Other" 6041,"Required annual income after taxes" 81429,"Annual taxes" 16356,"Required annual income before taxes" 97785}])

(def col-humbolt
  [
   {"Type" "1 Working Adult, 1 Child","Food" 5245,"Child Care" 8260,"Medical" 6828,"Housing" 12312,"Transportation" 7975,"Other" 4533,"Required annual income after taxes" 45153,"Annual taxes" 8581,"Required annual income before taxes" 53734}
   {"Type" "1 Working Adult, 2 Children","Food" 7893,"Child Care" 13911,"Medical" 6547,"Housing" 12312,"Transportation" 8373,"Other" 4999,"Required annual income after taxes" 54035,"Annual taxes" 10652,"Required annual income before taxes" 64686}
   {"Type" "2A 3C","Food" 12773,"Child Care" 19943,"Medical" 6389,"Housing" 26089,"Transportation" 10196,"Other" 6041,"Required annual income after taxes" 81429,"Annual taxes" 16356,"Required annual income before taxes" 97785}])

;; Source: wikipedia
(def income-data
  [["Alameda" 36439 73775 90822]
   ["Alpine" 24375 61343 71932]
   ["Amador" 27373 52964 68765]
   ["Butte" 24430 43165 56934]
   ["Calaveras" 29296 54936 67100]
   ["Colusa" 22211 50503 56472]
   ["Contra Costa" 38770 79799 95087]
   ["Del Norte" 19424 39302 52452]
   ["El Dorado" 35128 68507 84690]
   ["Fresno" 20231 45201 50046]
   ["Glenn" 21698 40106 51940]
   ["Humboldt" 23516 42153 53532]
   ["Imperial" 16409 41772 46555]
   ["Inyo" 27028 45625 69041]
   ["Kern" 20467 48574 52541]
   ["Kings" 18518 47341 50202]
   ["Lake" 21310 35997 47773]
   ["Lassen" 19847 53351 66717]
   ["Los Angeles" 27987 55870 62289]
   ["Madera" 17797 45490 49964]
   ["Marin" 58004 91529 120030]
   ["Mariposa" 28327 50560 63520]
   ["Mendocino" 23712 43290 53996]
   ["Merced" 18464 43066 47729]
   ["Modoc" 21830 38560 46536]
   ["Mono" 29578 61814 73494]
   ["Monterey" 25048 58582 62370]
   ["Napa" 35092 70925 81275]
   ["Nevada" 32117 56949 69649]
   ["Orange" 34416 75998 85472]
   ["Placer" 35711 73747 88615]
   ["Plumas" 29167 48032 60709]
   ["Riverside" 23660 56592 63523]
   ["Sacramento" 27071 55615 64496]
   ["San Benito" 26317 67874 71124]
   ["San Bernardino" 21384 54100 59626]
   ["San Diego" 31043 63996 74569]
   ["San Francisco" 49986 78378 93391]
   ["San Joaquin" 22642 53253 59614]
   ["San Luis Obispo" 30392 59454 75828]
   ["San Mateo" 47198 91421 108088]
   ["Santa Barbara" 30526 63409 73636]
   ["Santa Clara" 42666 93854 106401]
   ["Santa Cruz" 33050 66923 81495]
   ["Shasta" 23763 44556 55028]
   ["Sierra" 28030 43107 57708]
   ["Siskiyou" 22482 37495 46079]
   ["Solano" 29132 67341 77634]
   ["Sonoma" 33361 63799 76614]
   ["Stanislaus" 21729 49573 55357]
   ["Sutter" 23828 51527 58434]
   ["Tehama" 21002 42369 49731]
   ["Trinity" 23145 36862 49221]
   ["Tulare" 17888 42863 45296]
   ["Tuolumne" 26063 48493 58355]
   ["Ventura" 33308 77335 86890]
   ["Yolo" 28080 55508 75225]
   ["Yuba" 19586 45470 49560]])

;; http://livingwage.mit.edu/counties/06001
;; http://livingwage.mit.edu/states/06/locations
;; Required income after taxes
;; 1A, 1A 1C, 1A 2C, 1A 3C
;; (1 working) 2A,	2A 1C, 2 A (1 W), 2 Children	2 Adults (1 Working) 3 Children
;;2 Adults (1 Working Part Time) 1 Child*	2 Adults	2 Adults 1 Child	2 Adults 2 Children	2 Adults 3 Children
(def col-counties
  [["Alameda",29597,58917,67799,89292,44857,54119,59613,71196,nil,44857,62379,73524,90758],
   ["Alpine",19409,42993,51875,67596,31825,38195,43689,49500,nil,31825,46455,57600,69062],
   ["Amador",20789,44985,53867,70704,33313,40187,45681,52608,nil,33313,48447,59592,72170],
   ["Butte",20249,43917,52799,69216,32929,39119,44613,51120,nil,32929,47379,58524,70682],
   ["Calaveras",19577,43245,52127,68232,32161,38447,43941,50136,nil,32161,46707,57852,69698],
   ["Colusa",18641,42705,51587,67452,32725,37907,43401,49356,nil,32725,46167,57312,68918],
   ["Contra Costa",29597,58917,67799,89292,44857,54119,59613,71196,nil,44857,62379,73524,90758],
   ["Del Norte",21197,43521,52403,68580,33049,38723,44217,50484,nil,33049,46983,58128,70046],
   ["El Dorado",21017,45273,54155,71184,34033,40475,45969,53088,nil,34033,48735,59880,72650],
   ["Fresno",20417,43485,52367,68184,32689,38687,44181,50088,nil,32689,46947,58092,69650],
   ["Glenn",18821,42141,51023,65676,31177,37343,42837,47580,nil,31177,45603,56748,67142],
   ["Humboldt",20645,45153,54035,70860,33481,40355,45849,52764,nil,33481,48615,59760,72326],
   ["Imperial",18641,42705,51587,66564,31693,37907,43401,48468,nil,31693,46167,57312,68030],
   ["Inyo",20825,43377,52259,68424,32677,38579,44073,50328,nil,32677,46839,57984,69890],
   ["Kern",19853,42969,51851,67752,31981,38171,43665,49656,nil,31981,46431,57576,69218],
   ["Kings",19805,42477,51359,66684,31657,37679,43173,48588,nil,31657,45939,57084,68150],
   ["Lake",19673,43821,52703,69072,32605,39023,44517,50976,nil,32605,47283,58428,70538],
   ["Lassen",19997,43845,52727,68364,32461,39047,44541,50268,nil,32461,47307,58452,69830],
   ["Los Angeles",24233,51381,60263,78036,38521,46583,52077,59940,nil,38521,54843,65988,79502],
   ["Madera",20381,43557,52439,68424,32245,38759,44253,50328,nil,32245,47019,58164,69890],
   ["Marin",35357,69057,77939,100212,53113,64259,69753,82116,nil,53113,72519,83664,101678],
   ["Mariposa",19553,43209,52091,66804,32917,38411,43905,48708,nil,32917,46671,57816,68270],
   ["Mendocino",20849,45213,54095,70608,33493,40415,45909,52512,nil,33493,48675,59820,72074],
   ["Merced",18641,42153,51035,66588,31405,37355,42849,48492,nil,31405,45615,56760,68054],
   ["Modoc",18041,41013,49895,64980,31081,36215,41709,46884,nil,31081,44475,55620,66446],
   ["Mono",21773,47121,56003,70956,34921,42323,47817,52860,nil,34921,50583,61728,72422],
   ["Monterey",23777,49845,58727,77844,37777,45047,50541,59748,nil,37777,53307,64452,79310],
   ["Napa",24545,52017,60899,81000,38917,47219,52713,62904,nil,38917,55479,66624,82466],
   ["Nevada",22337,48549,57431,75960,36001,43751,49245,57864,nil,36001,52011,63156,77426],
   ["Orange",27461,54597,63479,83460,41413,49799,55293,65364,nil,41413,58059,69204,84926],
   ["Placer",21017,45273,54155,71184,34033,40475,45969,53088,nil,34033,48735,59880,72650],
   ["Plumas",19277,42813,51695,66276,32821,38015,43509,48180,nil,32821,46275,57420,67742],
   ["Riverside",21977,47205,56087,73272,35665,42407,47901,55176,nil,35665,50667,61812,74738],
   ["Sacramento",21017,45273,54155,71184,34033,40475,45969,53088,nil,34033,48735,59880,72650],
   ["San Benito",24605,50721,59603,79116,37777,45923,51417,61020,nil,37777,54183,65328,80582],
   ["San Bernardino",21977,47205,56087,73272,35665,42407,47901,55176,nil,35665,50667,61812,74738],
   ["San Diego",26921,53733,62615,83172,40285,48935,54429,65076,nil,40285,57195,68340,84638],
   ["San Francisco",35357,69057,77939,100212,53113,64259,69753,82116,nil,53113,72519,83664,101678],
   ["San Joaquin",19781,44445,53327,69984,32977,39647,45141,51888,nil,32977,47907,59052,71450],
   ["San Luis Obispo",23021,48549,57431,75960,36313,43751,49245,57864,nil,36313,52011,63156,77426],
   ["San Mateo",35357,69057,77939,100212,53113,64259,69753,82116,nil,53113,72519,83664,101678],
   ["Santa Barbara",25949,51501,60383,78624,40057,46703,52197,60528,nil,40057,54963,66108,80090],
   ["Santa Clara",30461,59481,68363,90024,45457,54683,60177,71928,nil,45457,62943,74088,91490],
   ["Santa Cruz",26297,54777,63659,82188,40681,49979,55473,64092,nil,40681,58239,69384,83654],
   ["Shasta",20261,43545,52427,68664,32437,38747,44241,50568,nil,32437,47007,58152,70130],
   ["Sierra",21941,46653,55535,70368,34573,41855,47349,52272,nil,34573,50115,61260,71834],
   ["Siskiyou",18569,42609,51491,66720,31525,37811,43305,48624,nil,31525,46071,57216,68186],
   ["Solano",22337,48369,57251,75696,36601,43571,49065,57600,nil,36601,51831,62976,77162],
   ["Sonoma",24941,51705,60587,80544,38737,46907,52401,62448,nil,38737,55167,66312,82010],
   ["Stanislaus",20201,44097,52979,69168,32893,39299,44793,51072,nil,32893,47559,58704,70634],
   ["Sutter",20333,43473,52355,68568,32329,38675,44169,50472,nil,32329,46935,58080,70034],
   ["Tehama",19217,42717,51599,66648,31609,37919,43413,48552,nil,31609,46179,57324,68114],
   ["Trinity",19673,42597,51479,67212,31525,37799,43293,49116,nil,31525,46059,57204,68678],
   ["Tulare",20333,43317,52199,68268,32197,38519,44013,50172,nil,32197,46779,57924,69734],
   ["Tuolumne",19757,44469,53351,68712,32929,39671,45165,50616,nil,32929,47931,59076,70178],
   ["Ventura",25793,53985,62867,83208,40093,49187,54681,65112,nil,40093,57447,68592,84674],
   ["Yolo",22841,46941,55823,73152,34789,42143,47637,55056,nil,34789,50403,61548,74618],
   ["Yuba",20333,43473,52355,68568,32329,38675,44169,50472,nil,32329,46935,58080,70034]
   ])

(def most-populated-counties
  {"Los Angeles" 10163507
   "San Diego" 3337685
   "Orange" 3190400
   "Riverside" 2423266
   "San Bernardino" 2157404
   "Santa Clara" 1938153
   "Alameda" 1663190
   "Sacramento" 1530615
   "Contra Costa" 1147439
   "Fresno" 989255
   "Kern" 893119
   "San Francisco" 884363
   "Ventura" 854223
   "San Mateo" 771410
   "San Joaquin" 745424
   "Stanislaus" 547899
   "Sonoma" 504217})
