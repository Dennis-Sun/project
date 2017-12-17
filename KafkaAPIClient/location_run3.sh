#!/bin/sh

#sudo sh -c "sync; echo 3 > /proc/sys/vm/drop_caches"
receive1=`grep eth0 /proc/net/dev | awk '{print $2}'`
transmit1=`grep eth0 /proc/net/dev | awk '{print $10}'`
read1=`grep vda1 /proc/diskstats | awk '{print $6}'`
write1=`grep vda1 /proc/diskstats | awk '{print $10}'`

start=$(date +%s)

java -cp ~/kafka_test/KafkaAPIClient/target/KafkaAPIClient-1.0-SNAPSHOT-jar-with-dependencies.jar com.spnotes.kafka.partition.LocationConsumer $1 group2 3 > ~/kafka_test/KafkaAPIClient/stdout_location.txt

end=$(date +%s)

receive2=`grep eth0 /proc/net/dev | awk '{print $2}'`
transmit2=`grep eth0 /proc/net/dev | awk '{print $10}'`
read2=`grep vda1 /proc/diskstats | awk '{print $6}'`
write2=`grep vda1 /proc/diskstats | awk '{print $10}'`

echo 'receive = '$(($receive2-$receive1)) > ~/kafka_test/KafkaAPIClient/stats_loc
echo 'transmit = '$(($transmit2-$transmit1)) >> ~/kafka_test/KafkaAPIClient/stats_loc
echo 'read = '$(($read2-$read1)) >> ~/kafka_test/KafkaAPIClient/stats_loc
echo 'write = '$(($write2-$write1)) >> ~/kafka_test/KafkaAPIClient/stats_loc
echo 'time = '$(($end-$start)) >> ~/kafka_test/KafkaAPIClient/stats_loc

