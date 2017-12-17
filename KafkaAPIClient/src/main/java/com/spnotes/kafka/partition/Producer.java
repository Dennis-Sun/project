package com.spnotes.kafka.partition;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;
//import java.util.Scanner;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

/**
 * Producer used to read a hdfs file and send its content
 */
public class Producer {
	//private static Scanner in;
	public static void main(String[] argv)throws Exception {
		if (argv.length != 2) {
			System.err.println("Please specify 2 parameters ");
			System.exit(-1);
		}
		String topicName = argv[0];
		//System.out.println("Enter message(type exit to quit)");

		//Configure the Producer
		Properties configProperties = new Properties();
		configProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
		configProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.ByteArraySerializer");
		configProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
		configProperties.put(ProducerConfig.BATCH_SIZE_CONFIG, "16384");
		configProperties.put(ProducerConfig.LINGER_MS_CONFIG, "500");  // 35
		//configProperties.put(ProducerConfig.LINGER_MS_CONFIG, "700"); 
		//configProperties.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, "200000");
		//configProperties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, "50000000"); // 33554432


		org.apache.kafka.clients.producer.Producer producer = new KafkaProducer(configProperties);
		//Path pt=new Path("/project/tmp/tmp00");
		String fileName = argv[1];
		Path pt = new Path("/project/tmp/" + fileName);
		//String uri = "hdfs://10.254.0.157/project/tmp/tmp0";
		Configuration conf = new Configuration();
		conf.set("fs.hdfs.impl", 
				org.apache.hadoop.hdfs.DistributedFileSystem.class.getName()
				);
		conf.set("fs.file.impl",
				org.apache.hadoop.fs.LocalFileSystem.class.getName()
				);
		FileSystem fs = FileSystem.get(new URI("hdfs://10.254.0.157:8020"), conf);
		BufferedReader br=new BufferedReader(new InputStreamReader(fs.open(pt)));

		try {
			String line;
			long runtime = 0;
			//double count = 0;
			line=br.readLine();
			while (line != null){
				long start = System.currentTimeMillis();
				ProducerRecord<String, String> rec = new ProducerRecord<String, String>(topicName,line);
				producer.send(rec);
				line = br.readLine();
				long end = System.currentTimeMillis();
				runtime += end - start;
				//count++;
			}
			//System.out.printf("Average time for sending messages = %f\n", runtime/count);
			System.out.printf("Total time for sending messages = %d millisecnods\n", runtime);
		} finally {
			br.close();
		}
		producer.close();
	}
}
