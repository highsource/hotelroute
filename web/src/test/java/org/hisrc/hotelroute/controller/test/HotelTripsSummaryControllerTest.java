package org.hisrc.hotelroute.controller.test;

import java.io.IOException;

import org.hisrc.hotelroute.controller.HotelTripsSummaryController;
import org.hisrc.hotelroute.dto.HotelTripsSummary;
import org.hisrc.hotelroute.service.HotelTripsSummaryService;
import org.junit.Assert;
import org.junit.Test;

import de.schildbach.pte.BahnProvider;
import de.schildbach.pte.NetworkProvider;

public class HotelTripsSummaryControllerTest {
	private NetworkProvider networkProvider = new BahnProvider();
	private HotelTripsSummaryService hotelTripsSummaryService = new HotelTripsSummaryService();
	private HotelTripsSummaryController hotelTripsSummaryController = new HotelTripsSummaryController();
	{
		hotelTripsSummaryService.setNetworkProvider(networkProvider);
		hotelTripsSummaryController
				.setHotelTripsSummaryService(hotelTripsSummaryService);
	}

	@Test
	public void succesfullyQueriesHotelTripsSummary() throws IOException {
		final HotelTripsSummary hotelTripsSummary = hotelTripsSummaryController
				.queryHotelTripsSummary(
						new String[] {
								"653392:Köpenicker Str. 80, 10179 Berlin - Mitte, Deutschland",
								"69593:Am Ostbahnhof 5, 10243 Berlin - Friedrichshain, Deutschland" },
						"Berlin U-Bahn Jannowitzbrücke",
						System.currentTimeMillis(), System.currentTimeMillis());
		Assert.assertNotNull(hotelTripsSummary);
	}
}
