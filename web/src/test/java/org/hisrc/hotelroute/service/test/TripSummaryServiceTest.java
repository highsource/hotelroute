package org.hisrc.hotelroute.service.test;

import java.io.IOException;
import java.util.Date;

import org.hisrc.hotelroute.dto.TripSummary;
import org.hisrc.hotelroute.service.TripSummaryService;
import org.junit.Assert;
import org.junit.Test;

import de.schildbach.pte.BahnProvider;
import de.schildbach.pte.NetworkProvider;
import de.schildbach.pte.dto.Location;

public class TripSummaryServiceTest {

	private NetworkProvider networkProvider = new BahnProvider();
	private TripSummaryService tripSummaryService = new TripSummaryService();
	{
		tripSummaryService.setNetworkProvider(networkProvider);
	}

	@Test
	public void successfullyQueriesLocationOfCataloniaBerlinMitte()
			throws IOException {
		final String address = "Köpenicker Str. 80, 10179 Berlin - Mitte, Deutschland";
		final Location location = tripSummaryService
				.queryLocation(address);
		Assert.assertNotNull(location);
		Assert.assertEquals("Berlin - Mitte", location.place);
		Assert.assertEquals("Köpenicker Straße 80", location.name);
	}

	@Test
	public void createsTripDate() {
		final Date correctedDate = tripSummaryService
				.createTripDate(new Date(0));
		Assert.assertEquals(
				(9 * 60 + correctedDate.getTimezoneOffset()) * 60 * 1000,
				correctedDate.getTime());
	}

	@Test
	public void successfullyQueriesTripSummary() throws IOException {
		final TripSummary tripSummary = tripSummaryService
				.queryTripSummary(
						69593L,
						"Am Ostbahnhof 5, 10243 Berlin - Friedrichshain, Deutschland",
						"Berlin U-Bahn Jannowitzbrücke", new Date(), new Date());
		Assert.assertNotNull(tripSummary);

	}
}
