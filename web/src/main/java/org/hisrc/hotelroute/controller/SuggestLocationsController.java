//package org.hisrc.hotelroute.controller;
//
//import java.io.IOException;
//
//import javax.inject.Inject;
//
//import org.hisrc.hotelroute.service.LocationService;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import de.schildbach.pte.dto.SuggestLocationsResult;
//
//@Controller
//public class SuggestLocationsController {
//
//	@Inject
//	private LocationService locationService;
//
//	@RequestMapping(value = "/suggestLocations", method = RequestMethod.GET)
//	@ResponseBody
//	public SuggestLocationsResult suggestLocation(
//			@RequestParam(value = "text", required = true) final String text)
//			throws IOException {
//		return locationService.suggestLocations(text);
//	}
//}
