package org.wdsi.app.kafkadebezium.utils;

public class GeoDataFormatter {

	public static String formatDebeziumValue (String value) {
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
	
	public static void main (String[] args) {
		
	}
}
