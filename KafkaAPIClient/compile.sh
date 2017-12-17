#!/bin/sh

mvn clean compile assembly:single

scp -r ~/kafka_test/KafkaAPIClient vm-25-2:kafka_test/.
scp -r ~/kafka_test/KafkaAPIClient vm-25-3:kafka_test/.
scp -r ~/kafka_test/KafkaAPIClient vm-25-4:kafka_test/.
scp -r ~/kafka_test/KafkaAPIClient vm-25-5:kafka_test/.
