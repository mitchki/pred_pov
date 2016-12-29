<!DOCTYPE html>
<meta charset="utf-8">
<style>

.counties {
  fill: none;
}

.states {
  fill: none;
  stroke: #fff;
  stroke-linejoin: round;
}



</style>

  <head>
    <link rel="stylesheet" type="text/css" href="http://fonts.googleapis.com/css?family=Montserrat">
    <style>
    h1 {font-family: Montserrat;}
    h1 {font-size: 350%;}
    </style>
  </head>


    <div id="chart_area" class="wrapper">
      <h1 id="title">Teen Pregnancy Rates by County</h1>
      <p class="space"></p>
    </div>

<svg width="960" height="600"></svg>
<script src="//d3js.org/d3.v4.min.js"></script>
<script src="//d3js.org/topojson.v1.min.js"></script>

<div id="demo">
    <div id="buttons">
    <button id="teen_preg">Teen Pregnancy</button>
    <button id="chlamydia">Chlamydia Rates</button>
    <button id="poverty">% Children in Poverty</button>
    <button id="uninsured">% Uninsured Need Cont.</button>
    <button id="adherents">% Religious Adherents</button>
    <button id="model">Model Predictions</button>
    <button id="high_risk">High Risk Counties</button>
    <button id="confed">Confederacy-Union</button>
    <br>
</div>

<script>

// Range function ala python
function Range(start, stop, step) {
    if (typeof stop == 'undefined') {
        // one param defined
        stop = start;
        start = 0;
    }

    if (typeof step == 'undefined') {
        step = 1;
    }

    if ((step > 0 && start >= stop) || (step < 0 && start <= stop)) {
        return [];
    }

    var result = [];
    for (var i = start; step > 0 ? i < stop : i > stop; i += step) {
        result.push(i);
    }

    return result;
};


var teen_preg_list = ["rgb(247,251,255)", "rgb(222,235,247)", "rgb(198,219,239)", "rgb(158,202,225)", "rgb(107,174,214)", "rgb(66,146,198)", "rgb(33,113,181)", "rgb(8,81,156)", "rgb(8,48,107)"]
var chlamydia_list = ["rgb(200,229,109)", "rgb(189,224,80)", "rgb(179,219,51)", "rgb(161,200,36)", "rgb(138,171,31)", "rgb(114,142,25)", "rgb(91,114,20)", "rgb(68,85,15)", "rgb(45,56,10)"]
var uninsured_list = ["rgb(237,187,153)", "rgb(232,168,124)", "rgb(227,149,95)", "rgb(222,130,66)", "rgb(216,111,38)", "rgb(187,96,33)", "rgb(158,81,28)", "rgb(129,66,23)", "rgb(100,51,18)"]
var poverty_list = ["rgb(200,252,255)", "rgb(132,247,255)", "rgb(13,240,255)", "rgb(0,219,234)", "rgb(0,188,200)", "rgb(0,156,166)", "rgb(0,124,132)", "rgb(0,92,98)", "rgb(0,60,64)"]
var adherents_list = ["rgb(190,109,229)", "rgb(177,80,224)", "rgb(164,51,219)", "rgb(147,36,200)", "rgb(125,31,171)", "rgb(104,25,142)", "rgb(83,20,114)", "rgb(62,15,85)", "rgb(41,10,56)"]

var teen_preg_scale = d3.scaleLinear()
	.domain(Range(0, 100, 100/9))
    .range(teen_preg_list)    

var svg = d3.select("svg"),
    width = +svg.attr("width"),
    height = +svg.attr("height");


var rateById = d3.map();
window.rateById = rateById

var projection = d3.geoAlbersUsa()
    .scale(1280)
    .translate([width / 2, height / 2]);


var path = d3.geoPath()
    .projection(projection);



function load_teen_preg(source) {
	d3.queue()
    	.defer(d3.json, "{{url_for('static', filename="us.json")}}")
    	.defer(d3.csv, "{{url_for('static', filename="map.csv")}}", function(d) { rateById.set(+d.id, d); })
    .await(function(error, us, my_csv) {
      if (error) throw error;
      
	for (var key in rateById) {
		for (var inner in rateById[key]) {

			if (rateById[key][inner].indexOf(',') != -1) { continue }
			else { rateById[key][inner] = +rateById[key][inner] }
		}
	};
	console.log(rateById)

	      
    svg.append("g")
        .attr("class", "counties")
      .selectAll("path")
        .data(topojson.feature(us, us.objects.counties).features)
      .enter().append("path")
      	.attr('class', 'cpath')
        .attr("d", path)
  	    .transition()
  	    .duration(2000)
  	    .attr('fill', function(d){return rateById['$' + d.id] == undefined ? '#DCDCDC' : rateById['$' + String(d.id)].preg_rate_color})

    svg.append("path")
        .datum(topojson.mesh(us, us.objects.states, function(a, b) { return a !== b; }))
        .attr("class", "states")
        .attr("d", path);
    });
};

load_teen_preg()

  d3.select("#teen_preg")
  	.on("click", function(d) { 
  	  	d3.selectAll(".cpath")
  	  	.transition(function(r) { return Math.random * 100; })
  	    .delay(function(d, i) { return i * 0.3; })
  	    .attr('fill', function(j){return rateById['$' + j.id] == undefined ? '#DCDCDC' : rateById['$' + String(j.id)].preg_rate_color})
  	    d3.select("#title")
  	    .text('Teen Births per 1,000')
})

  d3.select("#chlamydia")
  	.on("click", function(d) { 
  	  	d3.selectAll(".cpath")
  	  	.transition(function(r) { return Math.random * 100; })
  	    .delay(function(d, i) { return i * 0.3; })
  	    .attr('fill', function(j){return rateById['$' + j.id] == undefined ? '#DCDCDC' : rateById['$' + String(j.id)].chlamydia_rate_color})
  	    d3.select("#title")
  	    .text('Chlamydia Rate per 100,000')
})

  d3.select("#poverty")
  	.on("click", function(d) { 
  	  	d3.selectAll(".cpath")
  	  	.transition()
  	    .delay(function(d, i) { return i * 0.3; })
  	    .attr('fill', function(j){return rateById['$' + j.id] == undefined ? '#DCDCDC' : rateById['$' + String(j.id)].child_poverty_rate_color})
  	    d3.select("#title")
  	    .text('Child Poverty Rate %')
})

  d3.select("#uninsured")
  	.on("click", function(d) { 
  	  	d3.selectAll(".cpath")
  	  	.transition()
  	    .delay(function(d, i) { return i * 0.3; })
  	    .attr('fill', function(j){return rateById['$' + j.id] == undefined ? '#DCDCDC' : rateById['$' + String(j.id)].women_uninsured_rate_color})
  	    d3.select("#title")
  	    .text('Uninsured Women in Need of Contraceptives')
})

  d3.select("#adherents")
  	.on("click", function(d) { 
  	  	d3.selectAll(".cpath")
  	  	.transition()
  	    .delay(function(d, i) { return i * 0.3; })
  	    .attr('fill', function(j){return rateById['$' + j.id] == undefined ? '#DCDCDC' : rateById['$' + String(j.id)].adherents_rate_color})
  	    d3.select("#title")
  	    .text('Religious Adherents as % of Population')
})

  d3.select("#model")
  	.on("click", function(d) { 
  	  	d3.selectAll(".cpath")
  	  	.transition()
  	    .delay(function(d, i) { return i * 0.3; })
  	    .attr('fill', function(j){return rateById['$' + j.id] == undefined ? '#DCDCDC' : rateById['$' + String(j.id)].predicted_vals})
  	    d3.select("#title")
  	    .text('Model Predictions')
})

	
  d3.select("#high_risk")
  	.on("click", function(d) { 
  	  	d3.selectAll(".cpath")
  	  	.transition()
  	    .delay(function(d, i) { return i * 0.3; })
  	    .attr('fill', function(j){return rateById['$' + j.id] == undefined ? '#DCDCDC' : rateById['$' + String(j.id)].feature_rate_color})
  	    d3.select("#title")
  	    .text('High-Risk Counties')
})


  d3.select("#confed")
  	.on("click", function(d) { 
  	  	d3.selectAll(".cpath")
  	  	.transition()
  	    .delay(function(d, i) { return i * 0.3; })
  	    .attr('fill', function(j){return rateById['$' + j.id] == undefined ? '#DCDCDC' : rateById['$' + String(j.id)].civil_war})
  	    d3.select("#title")
  	    .text('High-Risk Counties')
})

</script>