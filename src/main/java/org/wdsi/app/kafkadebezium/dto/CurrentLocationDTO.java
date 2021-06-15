package org.wdsi.app.kafkadebezium.dto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	private double distance;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CurrentLocationDTO.class);
	
	public String toString () {
		return new StringBuffer(latitude).append(",").append(longitude)
				.append(",").append(truckId).toString();
	}
	
	public boolean amIValid () {
		return truckId !=null && latitude != null && longitude != null;
	}
	
	/*public CurrentLocationDTO add (CurrentLocationDTO dto) {
		if (dto == null) {
			LOGGER.info("dto is null");
			return new CurrentLocationDTO();
			
		} 
		if (longitude == null || latitude == null) {
			distance = 0d;
		} else {
			LOGGER.info("incoming dto: {} ", dto);
			double dt = getDistance(Double.parseDouble(latitude), 
					Double.parseDouble(longitude),
					Double.parseDouble(dto.getLatitude()), Double.parseDouble(dto.getLongitude()));
			distance+= dt;
		}

		if (geoTime == null) {
			geoTime = dto.getGeoTime();
		}
		return CurrentLocationDTO.builder().currentLatitude(dto.getLatitude()).currentLongitude(dto.getLongitude()).distance(distance)
				.startDate(startDate)
				.date(dto.getDate()).truckId(dto.getTruckId()).build();
	}*/
}
