var destination = document.getElementById("summary_destination").textContent;
console.log("Destination: " + destination);

var distancesObserver = new MutationObserver(function(mutations) {
	mutations.forEach(function(mutation) {
		for(var i = 0; i < mutation.addedNodes.length; i++)
		{
			var addedNode = mutation.addedNodes[i];
			if (addedNode.getAttribute("class") && addedNode.getAttribute("class").indexOf("distances") > -1)
			{
				var distancesNode = addedNode;
				console.log("Added distances node:");
				console.log(distancesNode);
				console.log("Tooltip data:");
				console.log(distancesNode.children[0].children[0].tooltipData);
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
	console.log(data);

	var hotelElement = document.getElementById(data.requestId);
	if(hotelElement) {
		var dists = hotelElement.getElementsByClassName('distances_centered');
		if(dists.length > 0) {
			var elem = dists[0];
			elem.innerHTML += '<div class="hd train" style="background-image:url(http://www.hotelroute.org/media/icon-time.png);background-position:2px 0;">' + ms2str(data.travelDuration) + '</div>';
			elem.innerHTML += '<div class="hd train" style="background-image:url(http://www.hotelroute.org/media/icon-walk.png);background-position:6px 0;">' + ms2str(data.walkDuration) + '</div>';
			elem.innerHTML += '<div class="hd train" style="background-image:url(http://www.hotelroute.org/media/icon-change.png);background-position:0px 0;">' + data.numChanges + ' umst.</div>';
		}
	}
}

function callAPIFunction(address, hotelId)
{
	var now = new Date().getTime();
	var url = 'http://api.hotelroute.org/queryTripSummary?to=' + encodeURIComponent(destination) + '&from=' + encodeURIComponent(address) + '&startDate=' + now + '&endDate=' + now + '&requestId=' + hotelId;
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
//				scrapeHRSHotelAddress(xhr.responseText);
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
//				console.log("Added hotel with id [" + hotelId + "].");

				var hotelItem = hotelNode.getAttribute("data-hotelitemurl");
				getHRSHotelAddress(hotelItem,hotelId);

				distancesObserver.observe(hotelNode, { childList: true });
			}
		}
	})
});


var target = document.getElementById("containerAllHotels");

var config = { childList: true };
 
hotelsObserver.observe(target, config);
