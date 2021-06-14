package org.wdsi.app.kafkadebezium.dto;

import org.apache.kafka.streams.KeyValue;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CurrentLocationDTO extends KeyValue<String, String> {

	



	private String truckId;
	private String geoTime;
	private String latitude;
	private String longitude;
	
	public CurrentLocationDTO(String key, String value) {
		super(key, value);
	}
	
	
	public static  KeyValue<String, String> getNew(String key, String value) {
		return null;
	}
}
