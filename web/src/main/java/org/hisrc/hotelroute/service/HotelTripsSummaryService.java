package org.hisrc.hotelroute.service;

import java.io.IOException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;

import org.apache.commons.lang3.Validate;
import org.hisrc.hotelroute.dto.HotelNumberAndAddress;
import org.hisrc.hotelroute.dto.HotelTripsSummary;
import org.hisrc.hotelroute.dto.TripSummary;
import org.springframework.stereotype.Service;

import de.schildbach.pte.NetworkProvider;
import de.schildbach.pte.NetworkProvider.Accessibility;
import de.schildbach.pte.NetworkProvider.WalkSpeed;
import de.schildbach.pte.dto.Location;
import de.schildbach.pte.dto.Product;
import de.schildbach.pte.dto.QueryTripsResult;
import de.schildbach.pte.dto.SuggestLocationsResult;
import de.schildbach.pte.dto.SuggestLocationsResult.Status;
import de.schildbach.pte.dto.Trip;
import de.schildbach.pte.dto.Trip.Individual;
import de.schildbach.pte.dto.Trip.Leg;

@Service
public class HotelTripsSummaryService {

	@Inject
	private NetworkProvider networkProvider;

	public void setNetworkProvider(NetworkProvider networkProvider) {
		this.networkProvider = networkProvider;
	}

	public HotelTripsSummary queryHotelTripsSummary(
			List<HotelNumberAndAddress> hotelNumberAndAddresses,
			String destinationAddress, Date startDate, Date endDate)
			throws IOException {

		final Location to = queryLocation(destinationAddress);

		if (to == null) {
			return null;
		} else {
			final Date date = createTripDate(startDate);
			final Map<Integer, Location> hotelNumberAndLocations = queryHotelLocations(hotelNumberAndAddresses);

			final Map<Integer, TripSummary> hotelNumberAndSummaries = new HashMap<Integer, TripSummary>(
					hotelNumberAndLocations.size());

			for (Entry<Integer, Location> hotelNumberAndLocationEntry : hotelNumberAndLocations
					.entrySet()) {
				final int hotelNumber = hotelNumberAndLocationEntry.getKey();
				final Location from = hotelNumberAndLocationEntry.getValue();
				try {
					final TripSummary tripSummary = queryTripSummary(from, to,
							date);
					hotelNumberAndSummaries.put(hotelNumber, tripSummary);
				} catch (IOException ignored) {
					// TODO
				}
			}
			return new HotelTripsSummary(to, hotelNumberAndSummaries);
		}
	}

	public Date createTripDate(Date date) {
		Validate.notNull(date);
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR, 9);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}

	public TripSummary queryTripSummary(Location from, Location to, Date date)
			throws IOException {
		final QueryTripsResult result = networkProvider.queryTrips(from, null,
				to, new Date(), true, Product.ALL, WalkSpeed.NORMAL,
				Accessibility.NEUTRAL, null);

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
			final TripSummary tripSummary = createTripSummary(tripWithShortestDuration);
			return tripSummary;
		}
	}

	private TripSummary createTripSummary(Trip trip) {
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
		return new TripSummary(trip.from, trip.to, walkDuration,
				travelDuration, trip.numChanges);
	}

	public Map<Integer, Location> queryHotelLocations(
			List<HotelNumberAndAddress> hotelNumberAndAddresses)
			throws IOException {
		Validate.noNullElements(hotelNumberAndAddresses);
		Map<Integer, Location> hotelLocations = new HashMap<Integer, Location>();
		for (HotelNumberAndAddress hotelNumberAndAddress : hotelNumberAndAddresses) {
			try {
				final Location hotelLocation = queryLocation(hotelNumberAndAddress.address);
				if (hotelLocation != null) {
					hotelLocations.put(hotelNumberAndAddress.hotelNumber,
							hotelLocation);
				}
			} catch (IOException ignored) {
				// TODO
			}
		}
		return hotelLocations;
	}

	public Location queryLocation(String text) throws IOException {

		final SuggestLocationsResult suggestLocationsResult = networkProvider
				.suggestLocations(text);
		if (suggestLocationsResult == null
				|| suggestLocationsResult.status != Status.OK
				|| suggestLocationsResult.getLocations().isEmpty()) {
			return null;
		} else {
			return suggestLocationsResult.getLocations().get(0);
		}
	}
}
