package org.hisrc.hotelroute.dto;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoricDelayMap {
	private HashMap<Date, Integer> delays;
	
	public void addDelay(Date date, Integer delay) {
		// bei mehreren Delays pro Tag nur die groesste Delay nehmen
		if (delays.containsKey(date)) {
			if (delays.get(date) < delay) {
				delays.remove(date);
				delays.put(date, delay);
			}
		} else {
			delays.put(date, delay);
		}
	}
	
	public HistoricDelayMap() {
		super();
		this.delays = new HashMap<Date, Integer>();
	}
	
	public HistoricDelayMap(List<Map<String, Object>> list) {
		for (Object o : list) {
			Map<String, Object> m = (Map<String, Object>) o;
			Integer delay = (Integer) m.get("verspaetung");
			Date date = (Date) m.get("betriebstag");
			
			addDelay(date, delay);
		}
	}
	
	public double getAverage() {
		// TODO
		return 0;
	}
	
	public double getMax() {
		// TODO
		return 0;
	}
	

	
}
