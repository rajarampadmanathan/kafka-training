package com.prajaram.kafka.brownbag;

import java.util.Arrays;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.KeyValueStore;

public class KafkaStreams {

	public void processStreams() {
		 Properties props = new Properties();
	        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "wordcount-application");
	        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
	        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
	        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
	 
	        StreamsBuilder builder = new StreamsBuilder();
	        KStream<String, String> textLines = builder.stream("multi-partition");
	        KTable<String, Long> wordCounts = textLines
	            .flatMapValues(textLine -> Arrays.asList(textLine.toLowerCase().split("\\W+")))
	            .groupBy((key, word) -> word)
	            .count(Materialized.<String, Long, KeyValueStore<Bytes, byte[]>>as("counts-store"));
	        wordCounts.toStream().to("test", Produced.with(Serdes.String(), Serdes.Long()));
	 
	        org.apache.kafka.streams.KafkaStreams streams = new org.apache.kafka.streams.KafkaStreams(builder.build(), props);
	        streams.start();
	}

	public static void main(String[] args) {
		new KafkaStreams().processStreams();
	}

}
