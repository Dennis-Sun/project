package com.spnotes.kafka.simple;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;

import java.util.Arrays;
import java.util.Properties;
import java.util.Scanner;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.URI;
import org.apache.hadoop.fs.FSDataOutputStream;


public class LocationConsumer {
	private static Scanner in;

	public static void main(String[] argv)throws Exception{
		if (argv.length != 2) {
			System.err.printf("Usage: %s <topicName> <groupId>\n",
					LocationConsumer.class.getSimpleName());
			System.exit(-1);
		}
		in = new Scanner(System.in);
		String topicName = argv[0];
		String groupId = argv[1];

		LocationConsumerThread consumerRunnable = new LocationConsumerThread(topicName,groupId);
		consumerRunnable.start();
		String line = "";
		while (!line.equals("exit")) {
			line = in.next();
		}
		consumerRunnable.getKafkaConsumer().wakeup();
		System.out.println("Stopping consumer .....");
		consumerRunnable.join();
	}

	private static class LocationConsumerThread extends Thread{
		private String topicName;
		private String groupId;
		private KafkaConsumer<String,String> kafkaConsumer;

		public LocationConsumerThread(String topicName, String groupId){
			this.topicName = topicName;
			this.groupId = groupId;
		}
		public void run() {
			Properties configProperties = new Properties();
			configProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
			configProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.ByteArrayDeserializer");
			configProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
			configProperties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
			configProperties.put(ConsumerConfig.CLIENT_ID_CONFIG, "simple");

			//Figure out where to start processing messages from
			kafkaConsumer = new KafkaConsumer<String, String>(configProperties);
			kafkaConsumer.subscribe(Arrays.asList(topicName));

			//Start processing messages

			try {
				Configuration conf = new Configuration();
				conf.set("fs.hdfs.impl",
						org.apache.hadoop.hdfs.DistributedFileSystem.class.getName()
					);
				conf.set("fs.file.impl",
						org.apache.hadoop.fs.LocalFileSystem.class.getName()
					);
				conf.setBoolean("dfs.support.append", true);

				FileSystem fs = FileSystem.get(new URI("hdfs://10.254.0.157:8020"), conf);
				String prefix = new String("/project/output/location/");

				while (true) {
					ConsumerRecords<String, String> records = kafkaConsumer.poll(100);
					for (ConsumerRecord<String, String> record : records) {

						String[] items = record.value().split("\\|");
						//String content = items[0];
						//String time = items[1];
						String location = items[2];
						// split time
						//System.out.println(time.split(" ").length);
						//System.out.println(record.value());
						//String hour = time.split(" ")[3].split(":")[0];

						Path file = new Path(prefix + location);
						if ( fs.exists( file )) { 
							FSDataOutputStream fs_append = fs.append(file);
							BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fs_append, "UTF-8"));
							writer.append(record.value() + '\n');
							//writer.flush();
							//fs_append.hflush();
							writer.close();
							fs_append.close();
						} else {
							FSDataOutputStream os = fs.create(file);
							BufferedWriter br = new BufferedWriter( new OutputStreamWriter( os, "UTF-8" ) );
							br.write(record.value() + '\n');
							//br.flush();
							//os.hflush();
							br.close();
							os.close();
						} 	

					} // for

				} // while 
			}catch(WakeupException ex){
				System.out.println("Exception caught " + ex.getMessage());

			}catch(Exception e){
				e.printStackTrace();
			}finally{
				kafkaConsumer.close();
				System.out.println("After closing KafkaConsumer");
			}
		}
		public KafkaConsumer<String,String> getKafkaConsumer(){
			return this.kafkaConsumer;
		}
	}
}

