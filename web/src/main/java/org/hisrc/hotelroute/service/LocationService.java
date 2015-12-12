package org.hisrc.hotelroute.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Service;

import de.schildbach.pte.dto.Location;
import de.schildbach.pte.dto.LocationType;
import de.schildbach.pte.dto.SuggestLocationsResult;
import de.schildbach.pte.dto.SuggestLocationsResult.Status;

@Service
public class LocationService {

	private static Set<LocationType> ALL = new HashSet<LocationType>(
			Arrays.asList(LocationType.ANY, LocationType.STATION,
					LocationType.POI, LocationType.ADDRESS));
	private static Set<LocationType> NON_STATIONS = new HashSet<LocationType>(
			Arrays.asList(LocationType.ANY, LocationType.POI,
					LocationType.ADDRESS));

	@Inject
	private NetworkProviderService networkProviderService;
	
	public void setNetworkProviderService(
			NetworkProviderService networkProviderService) {
		this.networkProviderService = networkProviderService;
	}

	public Location queryLocation(String constraint) throws IOException {
		return queryLocation(constraint, ALL);
	}

	public Location queryNonStationLocation(String constraint)
			throws IOException {
		return queryLocation(constraint, NON_STATIONS);
	}

	private Location queryLocation(String constraint, Set<LocationType> types)
			throws IOException {
		Validate.notNull(constraint);
		Validate.noNullElements(types);
		final SuggestLocationsResult suggestLocationsResult = networkProviderService
				.suggestLocations(constraint);
		if (suggestLocationsResult == null
				|| suggestLocationsResult.status != Status.OK) {
			return null;
		} else {
			final List<Location> originalSuggestedLocations = suggestLocationsResult
					.getLocations();
			if (originalSuggestedLocations.isEmpty()) {
				return null;
			} else {
				final List<Location> suggestedLocations = new ArrayList<Location>(
						suggestLocationsResult.getLocations());
				for (Iterator<Location> suggestedLocationsIterator = suggestedLocations
						.iterator(); suggestedLocationsIterator.hasNext();) {
					final Location suggestedLocation = suggestedLocationsIterator
							.next();
					if (!types.contains(suggestedLocation.type)) {
						suggestedLocationsIterator.remove();
					}
				}
				if (!suggestedLocations.isEmpty()) {
					return suggestedLocations.get(0);
				} else {
					return originalSuggestedLocations.get(0);
				}

			}
		}
	}
}
