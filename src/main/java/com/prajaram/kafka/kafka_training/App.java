package com.prajaram.kafka.kafka_training;

import java.util.concurrent.ExecutionException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws Exception
    {
        System.out.println( "Hello World!" );
        new Thread(()->{
			try {
				new SimpleProducer().produce(args);
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}).start();
        System.out.println("Init consumer");
        new Thread(()->{
			try {
		        new SimpleConsumer().consume(args,1, "test");
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}).start();
        new Thread(()->{
			try {
		        new SimpleConsumer().consume(args,2, "test");
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}).start();
        new Thread(()->{
			try {
		        new SimpleConsumer().consume(args,3, "test-1");
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}).start();
    }
}
