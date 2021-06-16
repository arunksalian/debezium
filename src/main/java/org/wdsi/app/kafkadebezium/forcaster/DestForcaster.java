package org.wdsi.app.kafkadebezium.forcaster;

import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.kafka.clients.consumer.ConsumerConfig;
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
	
	@PostConstruct
	public void init () {
		initStream ();
		
	}
	
	public void initStream () {
		LOGGER.info("initializing kafka stream");
		initializer = TruckStatus::new;
		Aggregator<String, org.wdsi.app.kafkadebezium.dto.GeoDTO, TruckStatus> aggregator = (k, v, i) -> i.add(v);
		final StreamsBuilder builder = new StreamsBuilder();
		
		KStream<String, String> stream = builder.stream(Constants.INPUT_TOPIC);
		
		stream.foreach((k, v)->{
			LOGGER.info("key:{}, value:{}", k, v);
		});
		/*KStream<String, String> stream = builder.stream(Constants.INPUT_TOPIC);
		stream.map((key, value)->KeyValue.pair(GeoDataFormatter.formatGeoKey(key), GeoDataFormatter.formatGeoValue(value)))
		.filter((k,v)->v !=null && v.amIValid()).groupByKey().aggregate(initializer, aggregator).toStream().foreach((k, v)->{
			LOGGER.info("key:{}, value:{}", k, v);
		});*/
		
		
		final Topology topology = builder.build();
		try (KafkaStreams geoStream = new KafkaStreams(topology, getProperties())) {
			geoStream.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
				 public void uncaughtException(Thread thread, Throwable throwable) {
					 LOGGER.error("Failed to start stream:{}", throwable.toString(), throwable);
				}
			});
			geoStream.start();
			LOGGER.info(" started stream details:{}", stream.toString());
		} catch (Exception e) {
			LOGGER.error("Failed to start stream.", e);
		}
	}
	
	private Properties getProperties() {
		Properties props = new Properties();
		props.put(StreamsConfig.APPLICATION_ID_CONFIG, "test-stream");
		props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		props.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		props.setProperty(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, StreamsConfig.EXACTLY_ONCE); // Enable exacly once feature

		props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
		props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());

		return props;
	}
}
