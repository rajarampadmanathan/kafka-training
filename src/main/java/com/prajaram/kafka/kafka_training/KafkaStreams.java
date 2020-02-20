package com.prajaram.kafka.kafka_training;

import java.util.Arrays;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;

public class KafkaStreams {

	public void processStreams() {
		Properties streamsConfig = new Properties();
		streamsConfig.put("bootstrap.servers", "localhost:9092");
		// producerConfig.put("acks", "all");
		streamsConfig.put("retries", 0);
		streamsConfig.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
		streamsConfig.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
		streamsConfig.put("retries", 0);
		streamsConfig.put(ConsumerConfig.GROUP_ID_CONFIG, "test");
		// streamsConfig.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
		// streamsConfig.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
		streamsConfig.put(StreamsConfig.APPLICATION_ID_CONFIG, "test-app");

		StreamsBuilder kStreamBuilder = new StreamsBuilder();
		KStream<String, String> textLines = kStreamBuilder.stream("global-event");
		KTable<String, Long> ktable = textLines.flatMapValues(v -> {
			System.out.println(v);
			return Arrays.asList(v.split(" "));
		}).groupBy((k, v) -> v).count();
		ktable.toStream().foreach((k, v) -> {
			System.out.println(k + ":" + v);
		});
		ktable.toStream().to("word-count", Produced.with(Serdes.String(), Serdes.Long()));
		org.apache.kafka.streams.KafkaStreams streams = new org.apache.kafka.streams.KafkaStreams(
				kStreamBuilder.build(), streamsConfig);
		streams.start();
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			System.out.println("Closing Stream");
			streams.close();
		}));
	}

	public static void main(String[] args) {
		new KafkaStreams().processStreams();
		Arrays.asList("a", "b", "a", "c", "Hello", "World", "Hello").stream().map(String::valueOf)
				.collect(Collectors.groupingBy(x -> x, Collectors.counting()))
				.forEach((a, b) -> System.out.println(a + "=" + b));
	}

}
