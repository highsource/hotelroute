package org.hisrc.hotelroute.dto;

import java.io.Serializable;
import java.util.Map;

import org.apache.commons.lang3.Validate;

import de.schildbach.pte.dto.Location;

public class HotelTripsSummary implements Serializable {

	private static final long serialVersionUID = 1L;
	public final Location destination;
	public final Map<Integer, TripSummary> tripSummaries;

	public HotelTripsSummary(Location destination,
			Map<Integer, TripSummary> tripSummaries) {
		this.destination = Validate.notNull(destination);
		this.tripSummaries = Validate.notNull(tripSummaries);
	}
}
