package org.hisrc.hotelroute.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.Validate;
import org.hisrc.hotelroute.dto.HotelNumberAndAddress;
import org.hisrc.hotelroute.dto.HotelTripsSummary;
import org.hisrc.hotelroute.service.HotelTripsSummaryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HotelTripsSummaryController {

	@Inject
	private HotelTripsSummaryService hotelTripsSummaryService;

	public void setHotelTripsSummaryService(
			HotelTripsSummaryService hotelTripsSummaryService) {
		this.hotelTripsSummaryService = hotelTripsSummaryService;
	}

	@RequestMapping(value = "/queryHotelTripsSummary", method = RequestMethod.GET)
	@ResponseBody
	public HotelTripsSummary queryHotelTripsSummary(
			@RequestParam(value = "from", required = true) final String[] from,
			@RequestParam(value = "to", required = true) final String to,
			@RequestParam(value = "startDate", required = false) final long startDateTimestamp,
			@RequestParam(value = "endDate", required = false) final long endDateTimestamp)
			throws IOException {

		Validate.noNullElements(from);
		Validate.notNull(to);

		final List<HotelNumberAndAddress> hotelNumberAndAddresses = new ArrayList<HotelNumberAndAddress>(
				from.length);

		for (String hotelNumberAndAddressAsString : from) {
			final int delimiterIndex = hotelNumberAndAddressAsString
					.indexOf(":");
			if (delimiterIndex > 0
					&& delimiterIndex < (hotelNumberAndAddressAsString.length() - 1)) {
				final int hotelNumber = Integer
						.parseInt(hotelNumberAndAddressAsString.substring(0,
								delimiterIndex));
				final String hotelAddress = hotelNumberAndAddressAsString
						.substring(delimiterIndex + 1);
				hotelNumberAndAddresses.add(new HotelNumberAndAddress(
						hotelNumber, hotelAddress));
			}
		}

		final Date startDate = new Date(startDateTimestamp);
		final Date endDate = new Date(endDateTimestamp);

		return hotelTripsSummaryService.queryHotelTripsSummary(
				hotelNumberAndAddresses, to, startDate, endDate);
	}
}
