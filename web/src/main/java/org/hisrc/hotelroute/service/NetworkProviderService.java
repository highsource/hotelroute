package org.hisrc.hotelroute.service;

import java.io.IOException;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import javax.annotation.Nullable;
import javax.inject.Inject;

import org.springframework.stereotype.Service;

import de.schildbach.pte.NetworkProvider;
import de.schildbach.pte.NetworkProvider.Accessibility;
import de.schildbach.pte.NetworkProvider.Option;
import de.schildbach.pte.NetworkProvider.WalkSpeed;
import de.schildbach.pte.dto.Location;
import de.schildbach.pte.dto.Product;
import de.schildbach.pte.dto.QueryTripsResult;
import de.schildbach.pte.dto.SuggestLocationsResult;

@Service
public class NetworkProviderService {

	@Inject
	private NetworkProvider networkProvider;

//	private ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(
//			1);
//
//	private Object mutex = new Object();
//
//	private long lastSchedule = System.currentTimeMillis();
//	private long standardDelay = 500;

	public QueryTripsResult queryTrips(final Location from,
			final @Nullable Location via, final Location to, final Date date,
			final boolean dep, final @Nullable Set<Product> products,
			final @Nullable WalkSpeed walkSpeed,
			final @Nullable Accessibility accessibility,
			final @Nullable Set<Option> options) throws IOException {

		final QueryTripsResult queryTripsResult = networkProvider.queryTrips(
				from, via, to, date, dep, products, walkSpeed, accessibility,
				options);
		if (queryTripsResult.status == QueryTripsResult.Status.SERVICE_DOWN) {
			throw new IOException("Service down (queryTrips).");
		} else {
			return queryTripsResult;
		}
	}

	public SuggestLocationsResult suggestLocations(final CharSequence constraint)
			throws IOException {
		final SuggestLocationsResult suggestLocationsResult = networkProvider
				.suggestLocations(constraint);

		if (suggestLocationsResult.status == SuggestLocationsResult.Status.SERVICE_DOWN) {
			throw new IOException("Service down (suggestLocations).");
		} else {
			return suggestLocationsResult;
		}

	}

	public void setNetworkProvider(NetworkProvider networkProvider) {
		this.networkProvider = networkProvider;
	}
}
