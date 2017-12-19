# QMS benchmarking for Data Ingestion
CS744 Class Project, Group 25, Fall 2017, UW-Madison

## Our Google Doc
[Link](https://docs.google.com/document/d/1Ye_3bJwN56e8-dQpvXinvqrP1m8jophdcGq9C7kqjrY/edit?usp=sharing)

## Team Members
- Haiyan Yang, hyang323@wisc.edu
- Huilin Hu, huilin.hu@wisc.edu
- Shuo Sun, ssun99@wisc.edu

## Naive Methods
- PartA_1.py: naive method A (naive Spark), split tweets using country code
- PartA_2.py: naive method A (naive Spark), split tweets using timestamp.hour
- PartB_1.py: naive method B (naive Spark), split tweets using country code
- PartB_1.py: naive method B (naive Spark), split tweets using timestamp.hour

To run any of the file above, simply try ./runtest_[a/b][1/2].sh [1/2/4/8/10/20/40/80]. The last parameter indicates the file size in GBs. Experiment results will be saved to /stats

Input data is located at [hdfs_root:]/project/tmp.

## Kafka Methods
Located at: /KafkaAPIClient/src/main/java/com/spnotes/kafka/partition
- Producer.java: Producer implementation
- TimeConsumer.java: Consumer implementation, split tweets using timestamp.hour
- LocationConsumer.java: Consumer implementation, split tweets using country code

To run this, cd to /KafkaAPIClient and run ./time_all_run.sh [topic_name]

The last parameter of the last line in time_all_run.sh is the input file name. The default directory is at [hdfs_root:]/project/tmp
