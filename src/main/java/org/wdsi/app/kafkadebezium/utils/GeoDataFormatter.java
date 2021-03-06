package org.wdsi.app.kafkadebezium.utils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wdsi.app.kafkadebezium.dto.GeoDTO;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GeoDataFormatter {

	private static final Logger LOGGER = LoggerFactory.getLogger(GeoDataFormatter.class);

	
	public static String formatDebeziumValue (String value) {
		LOGGER.info("Value::{}", value);
		if(value != null && !value.startsWith("{")) {
			int i = value.indexOf("{");
			if (i > 0) {
				return value.substring(i).trim();
			}
			
		}
		return value;
	}
	
	public static GeoDTO formatGeoValue (String value) {
		Map<String, Object> map = getJsonMap(formatDebeziumValue(value));
		Map<String, Object> afterMap =(Map<String, Object>) map.get("after");
		if (afterMap == null) {
			LOGGER.warn("Value not found");
			return GeoDTO.builder().build();
		}
		return GeoDTO.builder().latitude(afterMap.get("latitude").toString())
				.longitude(afterMap.get("longitude").toString())
				.truckId(afterMap.get("vehicleid").toString())
				.date(LocalDateTime.parse(afterMap.get("geo_time").toString()))
				.build();
	}
	public static String formatGeoKey (String value) {
		LOGGER.info("key :{}",value);
		Map<String, Object> map = getJsonMap(formatDebeziumValue(value));
		String vehicleId = map.get("vehicleid").toString();
		LOGGER.info("vehicle id extracted :{}",vehicleId);
		return vehicleId;
	}
	
	public static void main (String[] args) {
		
	}
	
	private static Map<String, Object> getJsonMap (String json) {
		ObjectMapper mapper = new ObjectMapper ();
		try {
			if (json != null)
				return mapper.readValue(json, Map.class);
		} catch (JsonProcessingException e) {
			LOGGER.error("Failed to parse json", e);
		}
		return new HashMap<String, Object>();
	}
}
