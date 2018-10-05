(ns calfresh-minisite.quotes)

(def quotes
  {"Placer" {:population "348,432"
             :center [-120.722718 39.06203]}

   "San Luis Obispo" {:population "269,637" :center [-120.447540 35.38522]},
   "Contra Costa" {:quotes
                   ["I need help with many basic day to today activities of daily living, because im disabled and have great difficulty getting things done"
                    "I was fired. In December I borowed from my 401K to pay off credit card debit, which I did."],
    :population "1,049,025"
    :center [-121.951543 37.91947]},

   "Marin" {:quotes
            ["I'm an unpaid intern working in Marin county for 6 months to complete my internship course credit for school"
             "I'm a single mom, divorced from someone that was highly abusive-mentally and physically."],
            :population "252,409"
            :center [-122.745974 38.05181]},
   "Napa" {:population "136,484" :center [-122.325995 38.50735]},
   "San Mateo" {:population "718,451" :center [-122.371542 37.41466]},
   "Tuolumne" {:population "55,365" :center [-119.964708 38.02145]},
   "Fresno" {:population "930,450" :center [-119.655019 36.761]},
   "Sierra" {:population "3,240" :center [-120.521993 39.57692]},
   "El Dorado" {:population "181,058" :center [-120.534398 38.78553]},
   "Lassen" {:population "34,895" :center [-120.629931 40.72108]},
   "Santa Barbara" {:population "423,895" :center [-120.038485 34.53737]},
   "Yolo" {:population "200,849" :center [-121.903178 38.67926]},
   "Yuba" {:population "72,155" :center [-121.344280 39.27002]},
   "Merced" {:population "255,793" :center [-120.722802 37.1948]},
   "Mono" {:population "14,202" :center [-118.875167 37.91583]},
   "San Francisco"
   {:center [-123.032229 37.72723],
    :quotes
    ["I work for a non-profit in San Francisco and I live in Oakland; therefore a big portion of my paycheck goes to transportation (roughly $360) a month."
     "I am currently going to college as a full time student, and I do not have full financial aid coverage"
     "Disability was declined. I really need help for food and other basic necessities."],
    :population "805,235"},
   "Amador" {:population "38,091" :center [-120.653856 38.44355]},
   "Mendocino" {:population "87,841" :center [-123.442881 39.43238]},
   "San Joaquin" {:population "685,306" :center [-121.272237 37.93503]},
   "Ventura" {:population "823,318" :center [-119.133143 34.35874]},
   "Kern" {:population "839,631" :center [-118.729506 35.34662]},
   "Inyo" {:population "18,546" :center [-117.403927 36.56197]},
   "Santa Clara" {:population "1,781,642" :center [-121.690622 37.22077]},
   "Orange" {:population "3,010,232" :center [-117.777207 33.67568]},
   "Solano" {:population "413,344" :center [-121.939594 38.26722]},
   "San Benito" {:population "55,269" :center [-121.085296 36.6107]},
   "San Diego" {:population "3,095,313" :center [-116.776117 33.0236]},
   "Trinity" {:population "13,786" :center [-123.114404 40.64772]},
   "Kings" {:population "152,982" :center [-119.815530 36.07247]},
   "Del Norte" {:population "28,610" :center [-123.980998 41.7499]},
   "Monterey" {:population "415,057" :center [-121.315573 36.2401]},
   "Santa Cruz" {:population "262,382" :center [-122.007205 37.01248]},
   "Modoc" {:population "9,686" :center [-120.718370 41.59291]},
   "Butte" {:population "220,000" :center [-121.601919 39.66595]},
   "Alameda"
   {:quotes
    ["I'm currently enrolled at UC Berkeley but my finances are very tight."
     "I'm struggling really bad, to the point of being in debt."
     "The rent is extremely high here and we receive absolutely no financial assistance from our parents"],
    :population "1,510,271"
    :lower-quartile-income "$36,439"
    :median-income "$73,775"
    :upper-quartile-income "$90,822"
    :minimum-cost-living-family "$73,524"
    :poverty-rate "12.00%"
    :center [-121.913304 37.64808]},
   "Plumas" {:population "20,007" :center [-120.829516 39.99517]},
   "Glenn" {:population "28,122" :center [-122.401700 39.60254]},
   "Colusa" {:population "21,419" :center [-122.237563 39.17773]},
   "Alpine" {:population "1,175" :center [-119.798999 38.61761]},
   "Humboldt" {:population "134,623" :center [-123.925818 40.70667]},
   "Los Angeles" {:population "9,818,605" :center [-118.261862 34.19639]},
   "Sonoma" {:population "483,878" :center [-122.945194 38.53257]},
   "Mariposa" {:population "18,251" :center [-119.912860 37.57003]},
   "Nevada" {:population "98,764" :center [-120.773446 39.29519]},
   "Stanislaus" {:population "514,453" :center [-121.002656 37.56238]},
   "Shasta" {:population "177,223" :center [-122.043550 40.76052]},
   "Sutter" {:population "94,737" :center [-121.702758 39.03525]},
   "Tulare" {:population "442,179" :center [-118.780542 36.23045]},
   "Riverside" {:population "2,189,641" :center [-116.002239 33.72982]},
   "Lake" {:population "64,665" :center [-122.746757 39.0948]},
   "Madera" {:population "150,865" :center [-119.749852 37.21003]},
   "Tehama" {:population "63,463" :center [-122.232276 40.12615]},
   "San Bernardino" {:population "2,035,210" :center [-116.181197 34.85722]},
   "Imperial" {:population "174,528" :center [-115.355395 33.04081]},
   "Calaveras" {:population "45,578" :center [-120.555115 38.18784]},
   "Siskiyou" {:population "44,900" :center [-122.533287 41.58798]},
   "Sacramento" {:population "1,418,788" :center [-121.340441 38.45001]}})
