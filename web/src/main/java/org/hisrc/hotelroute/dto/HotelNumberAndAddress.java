package org.hisrc.hotelroute.dto;

import java.io.Serializable;

import org.apache.commons.lang3.Validate;

public class HotelNumberAndAddress implements Serializable {

	private static final long serialVersionUID = 1L;
	public final int hotelNumber;
	public final String address;

	public HotelNumberAndAddress(int hotelNumber, String address) {
		this.hotelNumber = hotelNumber;
		this.address = Validate.notNull(address);
	}

}
