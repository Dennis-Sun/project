#!/bin/sh
vm=

vm=vm-25-2
 ssh  $vm " ~/kafka_test/KafkaAPIClient/time_run2.sh $1" &

#vm=vm-25-3
# ssh  $vm " ~/kafka_test/KafkaAPIClient/test_run.sh " &
#
#vm=vm-25-4
# ssh  $vm " ~/kafka_test/KafkaAPIClient/test_run.sh " &
#
#vm=vm-25-5
# ssh  $vm " ~/kafka_test/KafkaAPIClient/test_run.sh " &
#
#start=$(date +%s)
#
#sleep 2
#
#end=$(date +%s)
#
#echo 'time = '$(($end-$start)) >> ~/kafka_test/KafkaAPIClient/stats

