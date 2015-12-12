# HotelRoute

HotelRoute is a Google Chrome extension which helps you to find hotels with the best public transport connection to your destination.

It helps you to save time, save money, reduce travel stress and get better hotel quality.

## What it does

HotelRoute extends hotel search results of [hrs.de](http://www.hrs.de) or [hrs.com](http://www.hrs.com) and adds information on public transport connection between the found hotels and the travel destination.

![Screenshot of hrs.de with HotelRoute information](media/hrsde01.png)

HotelRoute adds the following information:

* ![8 min walk](media/walk01.png) - walking time to the next public transport stop
* ![21 min travel](media/travel01.png) - duration of the trip with public transport to your destination (in minutes)
* ![1 change](media/change01.png) - how many times do you have to change during the trip

## How it helps

When you search for hotels on portals like [hrs.com](http://www.hrs.com), [hotel.com](http://www.hotel.com) or [booking.com](http://www.booking.com),
search results typically contain distance to your destination in kilometers or miles.

If you prefer using public transport, distance in kilometers does not help you that much.
Depending on the location of your hotel and your travel destination, 10 kilometers may take you 10 minutes or over an hour.
This may be a direct connection or you may need to change transport means a couple of time.

All of this makes a great difference in terms of time and stress, and ultimately has great influence on yoru travel experience.

HotelRoute checks each of the found hotels for the quality of the public transport connection to you travel destination and displays this information directly in the search results.

This helps you to:

* save time by travelling less,
* save money by avoiding expensive central hotels,
* get better hotel quality (more hotel stars) for the price in offside, but still well connected hotels.

## How it works

* HotelRoute is a Google Chrome extension which works with hotel search on [hrs.de](http://www.hrs.de) or [hrs.com](http://www.hrs.com).
* 
Finds hotels with the best public transport connection to your destination.

## Disclaimer

The accuracy of the provided information is not guaranteed. We overtake nor responsibility for the correctness of results.

## Limitations

* HotelRoute uses [bahn.de](http://bahn.de) to find the shortest trip between each of the hotels and your travel destination.  
This means HotelRoute mostly works only for German destinations.
* HotelRoute uses "unofficial" APIs for [bahn.de](http://bahn.de), accesses and modifies [hrs.de](http://www.hrs.de)/[hrs.com](http://www.hrs.com) pages directly.  
This means HotelRoute may cease working or break any moment of time without notice.
* HotelRoute is primarily a prototype with the goal to demostrate integration of [bahn.de](http://bahn.de) with external applications.  
HotelRoute may be discontinued or shut down any moment of time.
* In some cases the location of the hotel can't be be unambiguously determined based on its address.  
This means that results may be inaccurate.
* HotelRoute searches for the shortest trip between the address of the hotel and your travel destination at ca. 09:00 of the first or the second day of the stay (the latter in case current time is already later than 09:00 of the first day of the stay).  
In certain cases (public holidays etc.) these results may be not representative.

## Privacy

* HotelRoute accesses travel destination, arrival and departure dates and the results of your search.
* This information is transmitted to the [api.hotelsearch.org](http://api.hotelsearch.org) and indirectly to [bahn.de](http://bahn.de).
* Currently we use the `HTTP` protocol to transfer data. This means that third parties potentially have access to the information indicated above.

## Contact

TODO
