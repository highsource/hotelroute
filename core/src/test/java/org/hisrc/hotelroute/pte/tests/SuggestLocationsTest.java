package org.hisrc.hotelroute.pte.tests;

import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import de.schildbach.pte.BahnProvider;
import de.schildbach.pte.dto.Location;
import de.schildbach.pte.dto.SuggestLocationsResult;

public class SuggestLocationsTest {
	private final BahnProvider provider = new BahnProvider();

	@Test
	public void testStation() throws IOException {
		final SuggestLocationsResult result = provider
				.suggestLocations("Berlin U-Bahn Jannowitzbrücke");
		final List<Location> locations = result.getLocations();
		Assert.assertFalse(locations.isEmpty());
		Location location = locations.get(0);
		Assert.assertEquals("8089019", location.id);
	}

	@Test
	public void testAddress() throws IOException {
		final SuggestLocationsResult result = provider
				.suggestLocations("Singerstr. 109, 10179 Berlin - Mitte, Deutschland");
		final List<Location> locations = result.getLocations();
		Assert.assertFalse(locations.isEmpty());
		Location location = locations.get(0);
		Assert.assertEquals("Singerstraße 109", location.name);
		Assert.assertEquals("Berlin - Mitte", location.place);
	}
}
