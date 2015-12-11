package org.hisrc.hotelroute.controller.test;

import java.io.IOException;

import org.hisrc.hotelroute.controller.TripSummaryController;
import org.hisrc.hotelroute.dto.TripSummary;
import org.hisrc.hotelroute.service.TripSummaryService;
import org.junit.Assert;
import org.junit.Test;

import de.schildbach.pte.BahnProvider;
import de.schildbach.pte.NetworkProvider;

public class TripSummaryControllerTest {
	private NetworkProvider networkProvider = new BahnProvider();
	private TripSummaryService tripSummaryService = new TripSummaryService();
	private TripSummaryController tripSummaryController = new TripSummaryController();
	{
		tripSummaryService.setNetworkProvider(networkProvider);
		tripSummaryController
				.setTripSummaryService(tripSummaryService);
	}

	@Test
	public void succesfullyQueriesTripSummary() throws IOException {
		final TripSummary tripSummary = tripSummaryController
				.queryTripSummary(
						653392L,
						"Köpenicker Str. 80, 10179 Berlin - Mitte, Deutschland",
						"Berlin U-Bahn Jannowitzbrücke",
						System.currentTimeMillis(), System.currentTimeMillis());
		Assert.assertNotNull(tripSummary);
	}
}
