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

function getHRSHotelAddress( hotelItem)
{
	var url = 'http://www.hrs.de' + hotelItem;
	var str = url;

	var xhr = new XMLHttpRequest();
	xhr.open('GET', url, true);
	xhr.onload = function (e) {
		if (xhr.readyState === 4) {
			if (xhr.status === 200) {
//				console.log(xhr.responseText);
				console.log(url);
			} else {
				console.error(xhr.statusText);
			}
		}
	};
	xhr.onerror = function (e) {
		console.error(xhr.statusText);
	};
	xhr.send(null);

	return str;
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
				var hotelAddress = getHRSHotelAddress( hotelItem);
				console.log("Added hotel with address [" + hotelAddress + "].");

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