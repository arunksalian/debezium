package org.wdsi.app.kafkadebezium.utils;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GeoDataFormatter {

	private static final Logger LOGGER = LoggerFactory.getLogger(GeoDataFormatter.class);
	public static String formatDebeziumValue (String value) {
		LOGGER.info("Value::{}", value);
		if(!value.startsWith("{")) {
			int i = value.indexOf("{");
			if (i > 0) {
				return value.substring(i).trim();
			}
			
		}
		return value;
	}
	
	public static String formatGeoValue (String value) {
		return value;
	}
	public static String formatGeoKey (String value) {
		Map<String, String> map = getJsonMap(formatDebeziumValue(value));
		return map.get("vehicleid");
	}
	
	public static void main (String[] args) {
		
	}
	
	private static Map<String, String> getJsonMap (String json) {
		ObjectMapper mapper = new ObjectMapper ();
		try {
			return mapper.readValue(json, Map.class);
		} catch (JsonProcessingException e) {
			LOGGER.error("Failed to parse json", e);
		}
		return new HashMap<String, String>();
	}
}
