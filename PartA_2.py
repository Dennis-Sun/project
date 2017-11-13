from __future__ import print_function

import sys
from pyspark.sql import SparkSession
from pyspark import SparkContext, SparkConf

def parseTweetsThruTimestamp(tweet):
    """Parses a line of tweet string."""
    parts = tweet.rsplit('|',3)
    minutes = parts[-2].rsplit(':',3) # take 'minutes' as the key
    return minutes[1], parts[0]

if __name__ == "__main__":
    if len(sys.argv) != 3:
        print >> sys.stderr, "Usage: PartA_2.py <inputpath> <outputpath>"
        exit(-1)

    # Initialize the spark context.
    conf = SparkConf().setAppName("Project-PartA-Scenario2")  \
		      .setMaster("spark://10.254.0.157:7077") \
		      .set("spark.eventLog.enabled", "true") \
		      .set("spark.eventLog.dir", "hdfs://10.254.0.157/eventLog")

    sc = SparkContext(conf=conf)
    spark = SparkSession(sc)

    # Loads input file. Partition based on location. The first item returned becomes the key automatically.
    tweets = sc.textFile(sys.argv[1], 20)
    #print("\n\nTWEETS: ", tweets.collect(), "\n\n")
    tweets = tweets.map(lambda tweet: parseTweetsThruTimestamp(tweet))
    #print("\n\nKEYS: ", tweets.keys().collect(), "\n\n")
    #print("\n\nTWEETS: ", tweets.collect(), "\n\n")
    tweets = tweets.distinct().groupByKey()

    # Write output using DataFrame, using Parquet format (the only option available for pyspark 2.0.0)
    tweetsDF = tweets.toDF(["timestamp", "text"])
    #tweetsDF.write.mode('overwrite').partitionBy("country").parquet(sys.argv[2])
    tweetsDF.write.save(sys.argv[2], "parquet", "overwrite", "timestamp")
    sc.stop()
