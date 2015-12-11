package org.hisrc.hotelroute.service.test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import org.hisrc.hotelroute.dto.HotelNumberAndAddress;
import org.hisrc.hotelroute.dto.HotelTripsSummary;
import org.hisrc.hotelroute.service.HotelTripsSummaryService;
import org.junit.Assert;
import org.junit.Test;

import de.schildbach.pte.BahnProvider;
import de.schildbach.pte.NetworkProvider;
import de.schildbach.pte.dto.Location;

public class HotelTripsSummaryServiceTest {

	private NetworkProvider networkProvider = new BahnProvider();
	private HotelTripsSummaryService hotelTripsSummaryService = new HotelTripsSummaryService();
	{
		hotelTripsSummaryService.setNetworkProvider(networkProvider);
	}

	@Test
	public void successfullyQueriesLocationsOfCataloniaBerlinMitte()
			throws IOException {

		final HotelNumberAndAddress hotelNumberAndAddress = new HotelNumberAndAddress(
				653392, "Köpenicker Str. 80, 10179 Berlin - Mitte, Deutschland");

		final Map<Integer, Location> locations = hotelTripsSummaryService
				.queryHotelLocations(Arrays.asList(hotelNumberAndAddress));
		Assert.assertNotNull(locations.get(653392));
	}

	@Test
	public void successfullyQueriesLocationOfCataloniaBerlinMitte()
			throws IOException {
		final String address = "Köpenicker Str. 80, 10179 Berlin - Mitte, Deutschland";
		final Location location = hotelTripsSummaryService
				.queryLocation(address);
		Assert.assertNotNull(location);
		Assert.assertEquals("Berlin - Mitte", location.place);
		Assert.assertEquals("Köpenicker Straße 80", location.name);
	}

	@Test
	public void createsTripDate() {
		final Date correctedDate = hotelTripsSummaryService
				.createTripDate(new Date(0));
		Assert.assertEquals(
				(9 * 60 + correctedDate.getTimezoneOffset()) * 60 * 1000,
				correctedDate.getTime());
	}

	@Test
	public void successfullyQueriesHotelTripsSummary() throws IOException {
		final HotelTripsSummary hotelTripsSummary = hotelTripsSummaryService
				.queryHotelTripsSummary(
						Arrays.asList(
								new HotelNumberAndAddress(653392,
										"Köpenicker Str. 80, 10179 Berlin - Mitte, Deutschland"),
								new HotelNumberAndAddress(69593,
										"Am Ostbahnhof 5, 10243 Berlin - Friedrichshain, Deutschland")),
						"Berlin U-Bahn Jannowitzbrücke", new Date(), new Date());
		Assert.assertNotNull(hotelTripsSummary);
		Assert.assertTrue(!hotelTripsSummary.tripSummaries.isEmpty());

	}
}
