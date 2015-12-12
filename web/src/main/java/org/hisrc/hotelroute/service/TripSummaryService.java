package org.hisrc.hotelroute.service;

import java.io.IOException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import javax.inject.Inject;

import org.apache.commons.lang3.Validate;
import org.hisrc.hotelroute.dto.TripSummary;
import org.springframework.stereotype.Service;

import de.schildbach.pte.NetworkProvider.Accessibility;
import de.schildbach.pte.NetworkProvider.WalkSpeed;
import de.schildbach.pte.dto.Location;
import de.schildbach.pte.dto.Product;
import de.schildbach.pte.dto.QueryTripsResult;
import de.schildbach.pte.dto.Trip;
import de.schildbach.pte.dto.Trip.Individual;
import de.schildbach.pte.dto.Trip.Leg;

@Service
public class TripSummaryService {

	@Inject
	private NetworkProviderService networkProviderService;

	@Inject
	private LocationService locationService;

	public void setNetworkProviderService(
			NetworkProviderService networkProviderService) {
		this.networkProviderService = networkProviderService;
	}

	public void setLocationService(LocationService locationService) {
		this.locationService = locationService;
	}

	public TripSummary queryTripSummary(Long requestId, String origin,
			String destination, Date startDate, Date endDate)
			throws IOException {

		final Location from = locationService.queryNonStationLocation(origin);
		final Location to = locationService.queryLocation(destination);

		if (from == null || to == null) {
			return null;
		} else {
			final Date date = createTripDate(startDate);
			return queryTripSummary(requestId, from, to, date);
		}
	}

	public Date createTripDate(Date date) {
		Validate.notNull(date);
		final Calendar calendar = Calendar.getInstance();
		long currentTimeInMillis = calendar.getTimeInMillis();
		calendar.setTimeInMillis(date.getTime());
		calendar.set(Calendar.HOUR, 9);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);

		while (calendar.getTimeInMillis() < currentTimeInMillis) {
			calendar.setTimeInMillis(calendar.getTimeInMillis()
					+ (24 * 60 * 60 * 1000));
		}

		return calendar.getTime();
	}

	public TripSummary queryTripSummary(Long requestId, Location from,
			Location to, Date date) throws IOException {
		final QueryTripsResult result = queryTrips(from, to, date);

		if (result == null || result.status != QueryTripsResult.Status.OK
				|| result.trips.isEmpty()) {
			// TODO
			return null;
		} else {
			final Trip tripWithShortestDuration = Collections.min(result.trips,
					new Comparator<Trip>() {
						public int compare(Trip one, Trip two) {
							if (one == two) {
								return 0;
							}
							if (one == null) {
								return 1;
							}
							if (two == null) {
								return -1;
							}
							return Long.compare(one.getDuration(),
									two.getDuration());
						}
					});
			final TripSummary tripSummary = createTripSummary(requestId,
					tripWithShortestDuration);
			return tripSummary;
		}
	}

	private QueryTripsResult queryTrips(Location from, Location to, Date date)
			throws IOException {
		final QueryTripsResult result = networkProviderService.queryTrips(from,
				null, to, date, true, Product.ALL, WalkSpeed.NORMAL,
				Accessibility.NEUTRAL, null);
		return result;
	}

	private TripSummary createTripSummary(Long requestId, Trip trip) {
		Validate.notNull(trip);
		final Leg firstLeg = trip.legs.get(0);
		Long walkDuration = null;
		if (firstLeg instanceof Individual) {
			final Individual individual = (Individual) firstLeg;
			if (individual.type == Trip.Individual.Type.WALK) {
				walkDuration = individual.getArrivalTime().getTime()
						- individual.getDepartureTime().getTime();
			}
		}
		final long travelDuration = trip.getDuration()
				- (walkDuration == null ? 0 : walkDuration);
		return new TripSummary(requestId, trip.from, trip.to, walkDuration,
				travelDuration, trip.numChanges);
	}

}
