package org.hisrc.hotelroute.dto;

import java.io.Serializable;

import org.apache.commons.lang3.Validate;

import de.schildbach.pte.dto.Location;

public class TripSummary implements Serializable {

	private static final long serialVersionUID = 364900051056665010L;

	public final Location from;
	public final Location to;
	public final int numChanges;
	public Long walkDuration;
	public long travelDuration;

	public TripSummary(Location from, Location to, Long firstWalkDuration,
			long travelDuration, int numChanges) {
		super();
		this.from = Validate.notNull(from);
		this.to = Validate.notNull(to);
		this.walkDuration = firstWalkDuration;
		this.travelDuration = Validate.notNull(travelDuration);
		this.numChanges = numChanges;
	}
}
