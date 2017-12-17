package com.spnotes.kafka.simple;

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

public class Producer {
	//private static Scanner in;
	public static void main(String[] argv)throws Exception {
		if (argv.length != 1) {
			System.err.println("Please specify 1 parameters ");
			System.exit(-1);
		}
		String topicName = argv[0];
		System.out.println("Enter message(type exit to quit)");

		//Configure the Producer
		Properties configProperties = new Properties();
		configProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
		configProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.ByteArraySerializer");
		configProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");

		org.apache.kafka.clients.producer.Producer producer = new KafkaProducer(configProperties);
		Path pt=new Path("/project/tmp/tmp00");
		//String uri = "hdfs://10.254.0.157/project/tmp/tmp0";
		Configuration conf = new Configuration();
		conf.set("fs.hdfs.impl", 
				org.apache.hadoop.hdfs.DistributedFileSystem.class.getName()
				);
		conf.set("fs.file.impl",
				org.apache.hadoop.fs.LocalFileSystem.class.getName()
				);
		//conf.addResource(new Path("/home/conf/core-site.xml"));
		//conf.addResource(new Path("/home/conf/hdfs-site.xml"));
		FileSystem fs = FileSystem.get(new URI("hdfs://10.254.0.157:8020"), conf);
		BufferedReader br=new BufferedReader(new InputStreamReader(fs.open(pt)));

		try {
			String line;
			line=br.readLine();
			while (line != null){
				ProducerRecord<String, String> rec = new ProducerRecord<String, String>(topicName,line);
				producer.send(rec);
				// be sure to read the next line otherwise you'll get an infinite loop
				line = br.readLine();
			}
		} finally {
			// you should close out the BufferedReader
			br.close();
		}
		/*
		   while(!line.equals("exit")) {
		//ProducerRecord<String, String> rec = new ProducerRecord<String, String>(topicName,line);
		//producer.send(rec);
		line = in.nextLine();
		}
		 */
		//in.close();
		producer.close();
	}
}
