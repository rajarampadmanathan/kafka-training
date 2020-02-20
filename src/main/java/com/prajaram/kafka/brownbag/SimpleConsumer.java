package com.prajaram.kafka.brownbag;

import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.requests.IsolationLevel;

public class SimpleConsumer {

	
	public static void consume(String[] args, int i, String groupName) throws InterruptedException, ExecutionException {
		System.out.println("Init Consumer...."+i);
		Properties consumerConfig = new Properties();
		consumerConfig.put("bootstrap.servers", "localhost:9092");
        consumerConfig.put("retries", 0);
		consumerConfig.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
		consumerConfig.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        consumerConfig.put(ConsumerConfig.GROUP_ID_CONFIG,groupName);
        consumerConfig.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,false);
        consumerConfig.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");
        consumerConfig.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG,1000);
        consumerConfig.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG,"read_committed");
		Consumer<String, String> consumer= new KafkaConsumer<>(consumerConfig);
		consumer.subscribe(Collections.singletonList("test"));
        while(true) {
        	consumer.poll(10).forEach(record->{
        		System.out.println("Consumer Group:"+groupName+"&& instance:"+i+" && Record Key:"+record.key()+" && Value:"+record.value()+" && Headers:"+record.headers()+"&& Partition:"+record.partition());
        		if(record.key().equals("1")) {
        		consumer.commitAsync();
        		}
        	});
        }
        //consumer.close();
	}
}
