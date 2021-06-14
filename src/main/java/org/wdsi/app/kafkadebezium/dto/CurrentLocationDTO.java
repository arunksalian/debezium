package org.wdsi.app.kafkadebezium.dto;

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
		return new StringBuffer(latitude).append(",").append(longitude)
				.append(",").append(truckId).toString();
	}
	
	public boolean amIValid () {
		return truckId !=null && latitude != null && longitude != null;
	}
	
	
}
