package com.spnotes.kafka.partition;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.TopicPartition;

import java.util.Collection;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Properties;
import java.util.HashMap;
import java.util.Set;
import java.lang.StringBuffer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.URI;
import org.apache.hadoop.fs.FSDataOutputStream;

/**
 * Consumer used to separate tweets by country code.
 */
public class LocationConsumer {

		public static void main(String[] argv)throws Exception{
				long start = System.currentTimeMillis();
				// check whether the number of input paramter is 3
				if (argv.length != 3) {
						System.err.printf("Usage: %s <topicName> <groupId> <consumerId>\n",
										LocationConsumer.class.getSimpleName());
						System.exit(-1);
				}

				String topicName = argv[0];
				String groupId = argv[1];
				String consumerId = argv[2];

				LocationConsumerThread consumerRunnable = new LocationConsumerThread(topicName,groupId, consumerId);
				consumerRunnable.start();
				consumerRunnable.join();
				long end = System.currentTimeMillis();
				System.out.printf("Stopping consumer %s_%s at %d\n", groupId, consumerId, end);
				System.out.printf("Total runteme for consumer %s_%s is %d\n", groupId, consumerId, end-start);
		}

		private static class LocationConsumerThread extends Thread{
				private String topicName;
				private String groupId;
				private String consumerId;
				private KafkaConsumer<String,String> kafkaConsumer;

				public LocationConsumerThread(String topicName, String groupId, String consumerId){
						this.topicName = topicName;
						this.groupId = groupId;
						this.consumerId = consumerId;
				}
				public void run() {
						Properties configProperties = new Properties();
						configProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
						configProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.ByteArrayDeserializer");
						configProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
						configProperties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
						configProperties.put(ConsumerConfig.CLIENT_ID_CONFIG, "partition");
						configProperties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");

						kafkaConsumer = new KafkaConsumer<String, String>(configProperties);
						kafkaConsumer.subscribe(Arrays.asList(topicName), new ConsumerRebalanceListener() {
										public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
										System.out.printf("%s topic-partitions are revoked from %s %s\n", Arrays.toString(partitions.toArray()), groupId, consumerId);
										}
										public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
										System.out.printf("%s topic-partitions are assigned from %s %s\n", Arrays.toString(partitions.toArray()), groupId, consumerId);
										}
										});

						//Start processing messages
						try {
								String prefix = new String("/project/output/location/consumer" + consumerId + "_");
								boolean done = false;
								boolean last = false;
								long totalSplit = 0;
								long totalWrite = 0;
								double count = 0;
								HashMap<String, StringBuffer> tweets = new HashMap<String, StringBuffer>();

								while (!done) {

										count++;
										ConsumerRecords<String, String> records = kafkaConsumer.poll(30000);
										System.out.println(last);
										// set done to true if empty records
										if (last && records.isEmpty()) {
												done = true;
												continue;
										} else {
												last = !records.isEmpty();
										}
										long splitTime = 0;
										// add each tweet to the hash map
										for (ConsumerRecord<String, String> record : records) {

												long start = System.currentTimeMillis();
												String line = record.value();
												String[] items = line.split("\\|");
												if (items.length < 3) {
														System.out.println(line);
														continue;
												}
												// extract contry code in the tweet
												String location = items[items.length-1];
												// add the tweet to the hash map according to its country code
												if(tweets.containsKey(location)) {
														StringBuffer cur = tweets.get(location);
														if (cur.length() > 40000000) {
																System.out.println("Large");
																totalWrite += writeHDFS(tweets, prefix);
																tweets = new HashMap<String, StringBuffer>();
														} else {
																tweets.put(location, cur.append(line).append("\n"));
														}
												} else {
														tweets.put(location, new StringBuffer(line).append("\n"));
												}
												long end = System.currentTimeMillis();
												splitTime += end - start;

										} // for
										totalSplit += splitTime;
										//System.out.printf("Processing time in loop %f = %d milliseconds\n", count, writeTime+splitTime);
								}
								totalWrite += writeHDFS(tweets, prefix);
								System.out.printf("Total split time = %d milliseconds\n", totalSplit);
								//System.out.printf("Average split time = %f milliseconds\n", totalSplit/count);
								System.out.printf("Total write time = %d milliseconds\n", totalWrite);
								//System.out.printf("Average write time = %f milliseconds\n", totalWrite/count);
						}catch(WakeupException ex){
								System.out.println("Exception caught " + ex.getMessage());

						}catch(Exception e){
								e.printStackTrace();
						}finally{
								kafkaConsumer.close();
								System.out.printf("After closing KafkaConsumer at %d\n", System.currentTimeMillis());
						}
				}
				public long writeHDFS(HashMap<String, StringBuffer> tweets, String prefix){
                        System.out.println("Start writing");
                        long writeTime = 0;
                        try {
                                // write all tweets to hdfs files according to its time
                                for (String key : tweets.keySet()) {
                                        long start = System.currentTimeMillis();
                                        Configuration conf = new Configuration();
                                        conf.set("fs.hdfs.impl",
                                                        org.apache.hadoop.hdfs.DistributedFileSystem.class.getName()
                                                );
                                        conf.set("fs.file.impl",
                                                        org.apache.hadoop.fs.LocalFileSystem.class.getName()
                                                );
                                        conf.setBoolean("dfs.support.append", true);

                                        FileSystem fs = FileSystem.get(new URI("hdfs://10.254.0.157:8020"), conf);
                                        Path file = new Path(prefix + key);
                                        FSDataOutputStream os = null;
                                        if ( fs.exists(file)) {
                                                os = fs.append(file);
                                        } else {
                                                os = fs.create(file);
                                        }
                                        BufferedWriter br = new BufferedWriter( new OutputStreamWriter( os, "UTF-8" ) );
                                        br.write(tweets.get(key).toString());
                                        br.flush();
                                        os.hflush();
                                        br.close();
                                        os.close();
                                        fs.close();
                                        long end = System.currentTimeMillis();
                                        writeTime += end - start;
                                        //System.out.printf("Finish writing %s file at time %d\n", key, System.currentTimeMillis());

                                } // for
                        } catch (Exception e) {
                                System.out.println("writeHDFS has somethings wrong\n" + e.getMessage());
                                System.exit(1);
                        }
                        return writeTime;

				}

				public KafkaConsumer<String,String> getKafkaConsumer(){
						return this.kafkaConsumer;
				}
		}
}

