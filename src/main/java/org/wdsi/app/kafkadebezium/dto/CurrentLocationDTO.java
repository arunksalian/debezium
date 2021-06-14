package org.wdsi.app.kafkadebezium.dto;

import org.apache.kafka.streams.KeyValue;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CurrentLocationDTO {

	private String truckId;
	private String geoTime;
	private String latitude;
	private String longitude;
	
	public String toString () {
		return latitude;
	}
	
	
}
