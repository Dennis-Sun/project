# QMS benchmarking for Data Ingestion
CS744 Class Project, Group 25, Fall 2017, UW-Madison

## Our Google Doc
[Link](https://docs.google.com/document/d/1Ye_3bJwN56e8-dQpvXinvqrP1m8jophdcGq9C7kqjrY/edit?ts=59d69257)

## Team Members
- Haiyan Yang, hyang323@wisc.edu
- Huilin Hu, huilin.hu@wisc.edu
- Shuo Sun, ssun99@wisc.edu

## Naive Methods
PartA_1.py: naive method A (naive Spark), split tweets using country code
PartA_2.py: naive method A (naive Spark), split tweets using timestamp.hour
PartB_1.py: naive method B (naive Spark), split tweets using country code
PartB_1.py: naive method B (naive Spark), split tweets using timestamp.hour

## Kafka Methods
Located at: /KafkaAPIClient/src/main/java/com/spnotes/kafka/partition
Producer.java: Producer implementation
TimeConsumer.java: Consumer implementation, split tweets using timestamp.hour
LocationConsumer.java: Consumer implementation, split tweets using country code
