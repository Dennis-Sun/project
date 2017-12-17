#!/bin/sh

receive1=`grep eth0 /proc/net/dev | awk '{print $2}'`
transmit1=`grep eth0 /proc/net/dev | awk '{print $10}'`
read1=`grep vda1 /proc/diskstats | awk '{print $6}'`
write1=`grep vda1 /proc/diskstats | awk '{print $10}'`

start=$(date +%s)

sleep 100 

end=$(date +%s)

receive2=`grep eth0 /proc/net/dev | awk '{print $2}'`
transmit2=`grep eth0 /proc/net/dev | awk '{print $10}'`
read2=`grep vda1 /proc/diskstats | awk '{print $6}'`
write2=`grep vda1 /proc/diskstats | awk '{print $10}'`

echo 'receive = '$(($receive2-$receive1)) > ~/kafka_test/KafkaAPIClient/stats
echo 'transmit = '$(($transmit2-$transmit1)) >> ~/kafka_test/KafkaAPIClient/stats
echo 'read = '$(($read2-$read1)) >> ~/kafka_test/KafkaAPIClient/stats
echo 'write = '$(($write2-$write1)) >> ~/kafka_test/KafkaAPIClient/stats
echo 'time = '$(($end-$start)) >> ~/kafka_test/KafkaAPIClient/stats

