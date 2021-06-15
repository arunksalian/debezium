package org.wdsi.app.kafkadebezium.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
//@NoArgsConstructor
@AllArgsConstructor
public class TruckStatus implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private String currentLongitude;
	private String currentLatitude;
	
	private double distance;
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime date;
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime startDate;
	private String truckId;
	public TruckStatus () {
		/*currentLongitude = "0";
		currentLatitude = "0";*/
	}
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TruckStatus.class);
	public TruckStatus add (GeoDTO dto) {
		if (dto == null) {
			LOGGER.info("dto is null");
			return new TruckStatus();
			
		} 
		if (currentLatitude == null || currentLongitude == null) {
			distance = 0d;
		} else {
			LOGGER.info("incoming dto: {} ", dto);
			double dt = getDistance(Double.parseDouble(currentLatitude), 
					Double.parseDouble(currentLongitude),
					Double.parseDouble(dto.getLatitude()), Double.parseDouble(dto.getLongitude()));
			distance+= dt;
		}

		if (startDate == null) {
			startDate = dto.getDate();
		}
		return TruckStatus.builder().currentLatitude(dto.getLatitude()).currentLongitude(dto.getLongitude()).distance(distance)
				.startDate(startDate)
				.date(dto.getDate()).truckId(dto.getTruckId()).build();
	}
	
	public String toString () {
	
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			LOGGER.error("error", e);
		}
		return "{}";
	}
	
	private double getDistance (double lat1, double lon1, double lat2,
	        double lon2) {
		final int R = 6371;
		
		double latDistance = Math.toRadians(lat2 - lat1);
	    double lonDistance = Math.toRadians(lon2 - lon1);
	    double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
	            + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
	            * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
	    return R * c * 1000; // convert to meters
	}
	
	private Double getSpeed () {
		
		//distance/time
		long time = ChronoUnit.SECONDS.between(startDate, date);
		double speed = distance/time;
		return speed*5/18;
	}
	
	public static void main (String[] args) {
		TruckStatus status = TruckStatus.builder().currentLatitude("11.0168").currentLongitude("76.9558").distance(0)
				.date(LocalDateTime.now())
				.startDate(LocalDateTime.now().minusHours(2)).build();
		GeoDTO dto=GeoDTO.builder().latitude("13.0827").longitude("80.2707").build();
		TruckStatus nStatus = status.add(dto);
		System.out.println("Distance = "+nStatus.getDistance()+", speed:"+status.getSpeed());
		
	}
}
