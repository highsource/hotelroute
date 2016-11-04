var destination = document.getElementById("summary_destination").textContent;
//console.log("Destination: " + destination);

var distancesObserver = new MutationObserver(function(mutations) {
	mutations.forEach(function(mutation) {
		for(var i = 0; i < mutation.addedNodes.length; i++)
		{
			var addedNode = mutation.addedNodes[i];
			if (addedNode.getAttribute("class") && addedNode.getAttribute("class").indexOf("distances") > -1)
			{
				for(var j = 0; j < mutation.target.attributes.length; ++j)
				{
					var attribute = mutation.target.attributes[j];
					if( 'data-hotelitemurl' == attribute.nodeName) {
						var hotelItem = attribute.nodeValue;
						var hotelId = mutation.target.id;
//						console.log("Added hotel with id [" + hotelId + "].");

						getHRSHotelAddress(hotelItem,hotelId);
					}
				}
			}
		}
	})
});

function ms2str(milliseconds)
{
	var ret = '';
	if(milliseconds) {
		milliseconds = parseInt( milliseconds / 1000 / 60);
		ret = milliseconds + ' min';
	} else {
		ret = '0 min';
	}

	return ret;
}

function injectResults(data)
{
//	console.log(data);

	var hotelElement = document.getElementById(data.requestId);
//	console.log(hotelElement);
	if(hotelElement) {
		var dists = hotelElement.getElementsByClassName('distances_centered');
//		console.log(dists.length);
		if(dists.length > 0) {
			var elem = dists[0];
			elem.innerHTML += '<div class="hd train" style="background-image:url(http://www.hotelroute.org/media/icon-walk.png);background-position:6px 0;">' + ms2str(data.walkDuration) + '</div>';
			elem.innerHTML += '<div class="hd train" style="background-image:url(http://www.hotelroute.org/media/icon-time.png);background-position:2px 0;">' + ms2str(data.travelDuration) + '</div>';
			elem.innerHTML += '<div class="hd train" style="background-image:url(http://www.hotelroute.org/media/icon-change' + (data.numChanges <=0 ? '1' : data.numChanges == 1 ? '2' : '3') + '.png);background-position:0px 0;">' + data.numChanges + ' umst</div>';
		}
	}
}

function callAPIFunction(address, hotelId)
{
	var now = new Date().getTime();
//	var url = 'http://api.hotelroute.org/queryTripSummary?to=' +
	var url = 'http://localhost:5000/queryTripSummary?to=' +
		encodeURIComponent(destination) + '&from=' + encodeURIComponent(address) + '&startDate=' + now + '&endDate=' + now + '&requestId=' + hotelId;
//	console.log(url);

	var xhr = new XMLHttpRequest();
	xhr.open('GET', url, true);
	xhr.onload = function (e) {
		if (xhr.readyState === 4) {
			if (xhr.status === 200) {
				if( '' != xhr.responseText) {
					var response = JSON.parse(xhr.responseText);
					injectResults(response)
				}
			} else {
//				console.error(xhr.statusText);
			}
		}
	};
	xhr.onerror = function (e) {
//		console.error(xhr.statusText);
	};
	xhr.send(null);
}

function scrapeHRSHotelAddress(html, hotelId)
{
	var div = document.createElement('div');
	div.innerHTML = html;

	var elements = div.getElementsByTagName('address');
	for(var i=0; i<elements.length; i++) {
		var address = elements[i];
		callAPIFunction(address.innerHTML, hotelId);
	}
}

function getHRSHotelAddress(hotelItem, hotelId)
{
	var url = 'http://www.hrs.de' + hotelItem;

	var xhr = new XMLHttpRequest();
	xhr.open('GET', url, true);
	xhr.onload = function (e) {
		if (xhr.readyState === 4) {
			if (xhr.status === 200) {
				scrapeHRSHotelAddress(xhr.responseText, hotelId);
			}
		}
	};
	xhr.send(null);
}

var hotelsObserver = new MutationObserver(function(mutations) {
	mutations.forEach(function(mutation) {
		for(var i = 0; i < mutation.addedNodes.length; i++)
		{
			var addedNode = mutation.addedNodes[i];
			if (addedNode.getAttribute("data-hpos"))
			{
				var hotelNode = addedNode;
				var hotelId = hotelNode.getAttribute("id");
				var dists = hotelNode.getElementsByClassName('distances_centered');
//				console.log("Added hotel with id [" + hotelId + "].");

				if(dists.length > 0) {
					var hotelItem = hotelNode.getAttribute("data-hotelitemurl");
					getHRSHotelAddress(hotelItem,hotelId);
				}

				distancesObserver.observe(hotelNode, { childList: true });
			}
		}
	})
});

var target = document.getElementById("containerAllHotels");
var config = { childList: true };
hotelsObserver.observe(target, config);

function addFilter(node)
{
	var str = '<div class="teaser"><div id="walking" class="sliderBox clearFix">';
	str += '<label style="width:auto;">Fußweg</label>';
	str += '<input type="text" class="noInput" readonly style="border:none;" value="0 - 20 min">';
	str += '<div class="slider " data-celname="Main Filter" data-celpos="5" data-celinfo="walking">';
	str += '<div class="knobs knob1" style="left: 0px; position: relative;">&nbsp;</div>';
	str += '<div class="knobs knob2" style="left: 170px; position: relative;">&nbsp;</div>';
	str += '<div class="leftEl" style="width: 0px;"></div>';
	str += '<div class="rightEl" style="width: 14px;"></div>';
	str += '</div>';
	str += '<div class="measure"><span class="left">0</span> <span class="right">20</span></div>';
	str += '</div></div>';

	node.insertAdjacentHTML('beforeend', str);
	node.insertBefore(node.childNodes[node.childNodes.length-1],node.childNodes[2]);

//	console.log(Filter);
//	console.log(MultiSliderFilter);
//	window.Filter.multiSliders.push("walking");
//	Filter.filter.push(new MultiSliderFilter($('walking'),Filter,Filter.sliderSettings['walking']));
//	new window.MultiSliderFilter(document.getElementById('walking'),window.Filter,{});
//	window.HotellistEventManager;
	console.log(chrome);
}

var filter = document.getElementById("filter");
//addFilter(filter);
