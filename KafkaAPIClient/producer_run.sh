#!/bin/sh

if hadoop fs -test -e /project/output/time; then
	echo "/project/output/time exists and create a new folder"
	hadoop fs -rm -r /project/output/time
	hadoop fs -mkdir /project/output/time
fi

if hadoop fs -test -e /project/output/location; then
	echo "/project/output/time exists and create a new folder"
	hadoop fs -rm -r /project/output/location
	hadoop fs -mkdir /project/output/location
fi

#~/software/kafka_2.11-1.0.0/bin/kafka-topics.sh --delete --zookeeper localhost:2181 --topic project
#~/software/kafka_2.11-1.0.0/bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 3 --partitions 20 --topic project

java -cp ~/kafka_test/KafkaAPIClient/target/KafkaAPIClient-1.0-SNAPSHOT-jar-with-dependencies.jar com.spnotes.kafka.partition.Producer project tmp1
