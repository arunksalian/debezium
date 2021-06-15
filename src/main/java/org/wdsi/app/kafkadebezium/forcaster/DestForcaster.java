package org.wdsi.app.kafkadebezium.forcaster;

import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Aggregator;
import org.apache.kafka.streams.kstream.Initializer;
import org.apache.kafka.streams.kstream.KStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.wdsi.app.kafkadebezium.dto.TruckStatus;
import org.wdsi.app.kafkadebezium.utils.Constants;
import org.wdsi.app.kafkadebezium.utils.GeoDataFormatter;

@Component
public class DestForcaster {
	private static final Logger LOGGER = LoggerFactory.getLogger(DestForcaster.class);
	
	private Initializer<TruckStatus> initializer;
	private Aggregator<String, org.wdsi.app.kafkadebezium.dto.GeoDTO, TruckStatus> aggregator = null;
	
	@PostConstruct
	public void init () {
		initStream ();
		initializer = TruckStatus::new;
	}
	
	public void initStream () {
		final StreamsBuilder builder = new StreamsBuilder();
		
		KStream<String, String> stream = builder.stream(Constants.INPUT_TOPIC);
		stream.map((key, value)->KeyValue.pair(GeoDataFormatter.formatGeoKey(key), GeoDataFormatter.formatGeoValue(value)))
		.filter((k,v)->v !=null && v.amIValid()).groupByKey().aggregate(initializer, aggregator);
		
		
		final Topology topology = builder.build();
		try (KafkaStreams geoStream = new KafkaStreams(topology, getProperties())) {
			
			geoStream.start();
		} catch (Exception e) {
			LOGGER.error("Failed to start stream.", e);
		}
	}
	
	private Properties getProperties() {
		Properties props = new Properties();
		props.put(StreamsConfig.APPLICATION_ID_CONFIG, "processor1");
		props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
		props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());

		return props;
	}
}
