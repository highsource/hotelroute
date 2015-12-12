package org.hisrc.hotelroute.service.test;

import java.io.IOException;

import org.hisrc.hotelroute.service.LocationService;
import org.hisrc.hotelroute.service.NetworkProviderService;
import org.junit.Assert;
import org.junit.Test;

import de.schildbach.pte.BahnProvider;
import de.schildbach.pte.NetworkProvider;
import de.schildbach.pte.dto.Location;

public class LocationServiceTest {

	private NetworkProvider networkProvider = new BahnProvider();
	private NetworkProviderService networkProviderService = new NetworkProviderService();
	private LocationService locationService = new LocationService();
	{
		networkProviderService.setNetworkProvider(networkProvider);
		locationService.setNetworkProviderService(networkProviderService);
	}

	@Test
	public void successfullyQueriesNonStationLocation() throws IOException {
		final String address = "Köpenicker Str. 80, 10179 Berlin - Mitte, Deutschland";
		final Location location = locationService
				.queryNonStationLocation(address);
		Assert.assertNotNull(location);
		Assert.assertEquals("Berlin - Mitte", location.place);
		Assert.assertEquals("Köpenicker Straße 80", location.name);
	}

	@Test
	public void successfullyQueriesLocationOfCataloniaBerlinMitte()
			throws IOException {
		final String address = "Berlin U-Bahn Jannowitzbrücke";
		final Location location = locationService
				.queryLocation(address);
		Assert.assertNotNull(location);
		Assert.assertEquals("Berlin Jannowitzbrücke", location.name);
	}
}
