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

function scrapeHRSHotelAddress( html)
{
	var div = document.createElement('div');
	div.innerHTML = html;
//	var elements = div.childNodes;
//
//	console.log(elements);

	var elements = div.getElementsByTagName('address');
	for(var i=0; i<elements.length; i++) {
		var address = elements[i];
		console.log(address.innerHTML);
	}
}

function getHRSHotelAddress( hotelItem)
{
	var url = 'http://www.hrs.de' + hotelItem;

	var xhr = new XMLHttpRequest();
	xhr.open('GET', url, true);
	xhr.onload = function (e) {
		if (xhr.readyState === 4) {
			if (xhr.status === 200) {
				console.log(url);
				scrapeHRSHotelAddress(xhr.responseText);
			} else {
				console.error(xhr.statusText);
			}
		}
	};
	xhr.onerror = function (e) {
		console.error(xhr.statusText);
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
				console.log("Added hotel with id [" + hotelId + "].");

				var hotelItem = hotelNode.getAttribute("data-hotelitemurl");
				getHRSHotelAddress( hotelItem);

				distancesObserver.observe(hotelNode, { childList: true });
			}
		}
	})
});


var target = document.getElementById("containerAllHotels");

var config = { childList: true };
 
hotelsObserver.observe(target, config);

var destination = document.getElementById("summary_destination").textContent;

console.log("Destination: " + destination);