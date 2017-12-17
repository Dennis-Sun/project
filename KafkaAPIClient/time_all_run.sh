#!/bin/sh

#mvn clean compile assembly:single

#scp -r ~/kafka_test/KafkaAPIClient vm-25-2:kafka_test/.
#scp -r ~/kafka_test/KafkaAPIClient vm-25-3:kafka_test/.
#scp -r ~/kafka_test/KafkaAPIClient vm-25-4:kafka_test/.
#scp -r ~/kafka_test/KafkaAPIClient vm-25-5:kafka_test/.


if hadoop fs -test -e /project/output/time; then
	echo "/project/output/time exists and create a new folder"
	hadoop fs -rm -r /project/output/time
	hadoop fs -mkdir /project/output/time
fi

if hadoop fs -test -e /project/output/location; then
	echo "/project/output/location exists and create a new folder"
	hadoop fs -rm -r /project/output/location
	hadoop fs -mkdir /project/output/location
fi

#~/software/kafka_2.11-1.0.0/bin/kafka-topics.sh --delete --zookeeper localhost:2181 --topic project1
~/software/kafka_2.11-1.0.0/bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 3 --partitions 20 --topic $1

vm=vm-25-2
ssh $vm "~/kafka_test/KafkaAPIClient/time_run2.sh $1" &
ssh $vm "~/kafka_test/KafkaAPIClient/location_run2.sh $1" &

vm=vm-25-3
ssh $vm "~/kafka_test/KafkaAPIClient/time_run3.sh $1" &
ssh $vm "~/kafka_test/KafkaAPIClient/location_run3.sh $1" &

vm=vm-25-4
ssh $vm "~/kafka_test/KafkaAPIClient/time_run4.sh $1" &
ssh $vm "~/kafka_test/KafkaAPIClient/location_run4.sh $1" &

vm=vm-25-5
ssh $vm "~/kafka_test/KafkaAPIClient/time_run5.sh $1" &
ssh $vm "~/kafka_test/KafkaAPIClient/location_run5.sh $1" &

~/kafka_test/KafkaAPIClient/time_run.sh $1 &
~/kafka_test/KafkaAPIClient/location_run.sh $1 &

java -cp ~/kafka_test/KafkaAPIClient/target/KafkaAPIClient-1.0-SNAPSHOT-jar-with-dependencies.jar com.spnotes.kafka.partition.Producer $1 tmp_1g
