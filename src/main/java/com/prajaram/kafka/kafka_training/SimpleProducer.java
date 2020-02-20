package com.prajaram.kafka.kafka_training;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.header.internals.RecordHeaders;

public class SimpleProducer {

	public static void produce(String[] args) throws InterruptedException, ExecutionException {
		Properties producerConfig = new Properties();
		producerConfig.put("bootstrap.servers", "localhost:9092");
		// producerConfig.put("acks", "all");
		// idempotency
		producerConfig.put("retries", 3);
		producerConfig.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
				"org.apache.kafka.common.serialization.StringSerializer");
		producerConfig.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
				"org.apache.kafka.common.serialization.StringSerializer");
		producerConfig.put(ProducerConfig.SEND_BUFFER_CONFIG,1);
		producerConfig.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG,"1");
		Producer<String, String> producer = new KafkaProducer<>(producerConfig);
		producer.initTransactions();
		producer.beginTransaction();
		for (int i = 0; i < 1000; i++) {
			producer.send(new ProducerRecord<String, String>("user-event", "test message from java " + i)).get();
			if (i % 100 == 0) {
				Headers headers= new RecordHeaders();
				headers.add("test-header", "test-head value".getBytes());
				
				ProducerRecord<String, String> pr = new ProducerRecord<>("test",String.valueOf(i),//Deciding factor for partition.
			
						"Global test message from java " + i / 100);
				System.out.println("Global Record...."+i);
				producer.send(pr,(meta,ex)->{
					System.out.println("Published "+meta.serializedKeySize()+"to partition:"+meta.partition());
					System.out.println("Went to partition:"+meta.partition());
				});
			}
			//producer.flush();
		}
		producer.commitTransaction();
		producer.close();
		System.out.println("Sent");
	}
}
