package org.hisrc.hotelroute.pte.tests;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import de.schildbach.pte.BahnProvider;
import de.schildbach.pte.NetworkProvider.Accessibility;
import de.schildbach.pte.NetworkProvider.WalkSpeed;
import de.schildbach.pte.dto.Location;
import de.schildbach.pte.dto.Product;
import de.schildbach.pte.dto.QueryTripsResult;
import de.schildbach.pte.dto.SuggestLocationsResult;
import de.schildbach.pte.dto.Trip;
import de.schildbach.pte.dto.Trip.Individual;
import de.schildbach.pte.dto.Trip.Leg;

public class QueryTripsTest {
	private final BahnProvider provider = new BahnProvider();

	@Test
	public void testQueryTrips() throws IOException {
		final SuggestLocationsResult hotelLocationsResult = provider
				.suggestLocations("Singerstr. 109, 10179 Berlin - Mitte, Deutschland");
		final List<Location> hotelLocations = hotelLocationsResult
				.getLocations();
		Assert.assertFalse(hotelLocations.isEmpty());
		final Location hotelLocation = hotelLocations.get(0);

		final SuggestLocationsResult destinationLocationsResult = provider
				.suggestLocations("Berlin U-Bahn Jannowitzbrücke");
		final List<Location> destinationLocations = destinationLocationsResult
				.getLocations();
		Assert.assertFalse(destinationLocations.isEmpty());
		final Location destinationLocation = destinationLocations.get(0);

		final QueryTripsResult result = provider.queryTrips(hotelLocation,
				null, destinationLocation, new Date(), true, Product.ALL,
				WalkSpeed.NORMAL, Accessibility.NEUTRAL, null);
		Assert.assertFalse(result.trips.isEmpty());

		final Trip trip = result.trips.get(0);
		int numChanges = trip.numChanges;
		boolean firstLeg = true;
		long walkDuration = 0;
		for (Leg leg : trip.legs) {
			if (firstLeg) {
				firstLeg = false;
				if (leg instanceof Individual) {
					final Individual individual = (Individual) leg;
					if (individual.type == Trip.Individual.Type.WALK) {
						walkDuration = individual.getArrivalTime().getTime()
								- individual.getDepartureTime().getTime();
					}
				}
			}
		}
		long travelDuration = trip.getDuration() - walkDuration;
	}
}
