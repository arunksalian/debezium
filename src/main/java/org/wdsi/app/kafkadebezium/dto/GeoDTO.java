package org.wdsi.app.kafkadebezium.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.apache.kafka.common.protocol.types.Field.Str;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeoDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime date;
	private String longitude;
	private String latitude;
	private String truckId;
	
	
	public String toString () {
		StringBuilder buffer = new StringBuilder(truckId)
				.append(",").append(longitude).append(latitude).append(date);
		return buffer.toString();
	}
	
	public boolean amIValid () {
		return truckId !=null && latitude != null && longitude != null;
	}
	
}
