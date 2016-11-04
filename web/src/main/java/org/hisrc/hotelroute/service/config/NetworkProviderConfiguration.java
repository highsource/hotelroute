package org.hisrc.hotelroute.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.schildbach.pte.BahnProvider;
import de.schildbach.pte.NetworkProvider;

@Configuration
public class NetworkProviderConfiguration {
	
	@Bean
	public NetworkProvider networkProvider()
	{
		return new BahnProvider();
	}

}
