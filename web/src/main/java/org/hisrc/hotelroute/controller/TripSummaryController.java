package org.hisrc.hotelroute.controller;

import java.io.IOException;
import java.util.Date;

import javax.inject.Inject;

import org.apache.commons.lang3.Validate;
import org.hisrc.hotelroute.dto.TripSummary;
import org.hisrc.hotelroute.service.TripSummaryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TripSummaryController {

	@Inject
	private TripSummaryService tripsSummaryService;

	public void setTripSummaryService(
			TripSummaryService tripSummaryService) {
		this.tripsSummaryService = tripSummaryService;
	}

	@RequestMapping(value = "/queryTripSummary", method = RequestMethod.GET)
	@ResponseBody
	public TripSummary queryTripSummary(
			@RequestParam(value = "requestId", required = false) final Long requestId,
			@RequestParam(value = "from", required = true) final String from,
			@RequestParam(value = "to", required = true) final String to,
			@RequestParam(value = "startDate", required = false) final long startDateTimestamp,
			@RequestParam(value = "endDate", required = false) final long endDateTimestamp)
			throws IOException {

		Validate.notNull(from);
		Validate.notNull(to);

		final Date startDate = new Date(startDateTimestamp);
		final Date endDate = new Date(endDateTimestamp);

		return tripsSummaryService.queryTripSummary(requestId, from,
				to, startDate, endDate);
	}
}
