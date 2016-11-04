package org.hisrc.hotelroute.service.test;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.hisrc.hotelroute.dto.TripSummary;
import org.hisrc.hotelroute.service.LocationService;
import org.hisrc.hotelroute.service.NetworkProviderService;
import org.hisrc.hotelroute.service.TripSummaryService;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import de.schildbach.pte.BahnProvider;
import de.schildbach.pte.NetworkProvider;

public class TripSummaryServiceTest {

	private NetworkProvider networkProvider = new BahnProvider();
	private NetworkProviderService networkProviderService = new NetworkProviderService();
	private LocationService locationService = new LocationService();
	private TripSummaryService tripSummaryService = new TripSummaryService();
	{
		networkProviderService.setNetworkProvider(networkProvider);
		locationService.setNetworkProviderService(networkProviderService);
		tripSummaryService.setNetworkProviderService(networkProviderService);
		tripSummaryService.setLocationService(locationService);
	}

	@Ignore
	@Test
	public void successfullyQueriesTripSummary() throws IOException {
		final TripSummary tripSummary = tripSummaryService.queryTripSummary(
				69593L,
				"Am Ostbahnhof 5, 10243 Berlin - Friedrichshain, Deutschland",
				"Berlin U-Bahn Jannowitzbrücke", new Date(), new Date());
		Assert.assertNotNull(tripSummary);

	}

	@Ignore
	@Test
	public void successfullyQueriesTripSummaries() throws IOException,
			InterruptedException, ExecutionException {

		final ExecutorService executorService = Executors.newFixedThreadPool(4);
		final Collection<Future<TripSummary>> tripSummaryFutures = new LinkedList<Future<TripSummary>>();
		for (int index = 0; index < 10; index++) {

			final Future<TripSummary> tripSummaryFuture = executorService
					.submit(new Callable<TripSummary>() {
						public TripSummary call() throws IOException {
							final TripSummary tripSummary = tripSummaryService
									.queryTripSummary(
											69593L,
											"Am Ostbahnhof 5, 10243 Berlin - Friedrichshain, Deutschland",
											"Berlin U-Bahn Jannowitzbrücke",
											new Date(), new Date());
							return tripSummary;
						}
					});

			tripSummaryFutures.add(tripSummaryFuture);
		}
		for (Future<TripSummary> tripSummaryFuture : tripSummaryFutures) {
			Assert.assertNotNull(tripSummaryFuture.get());
		}
	}
}
