from __future__ import print_function

import sys
from pyspark.sql import SparkSession
from pyspark import SparkContext, SparkConf

def parseTweetsThruLocation(tweet):
    """Parses a line of tweet string."""
    parts = tweet.rsplit('|',3)
    return parts[2], parts[0]

if __name__ == "__main__":
    if len(sys.argv) != 3:
        print >> sys.stderr, "Usage: PartA_1.py <inputpath> <outputpath>"
        exit(-1)

    # Initialize the spark context.
    conf = SparkConf().setAppName("Project-PartA-Scenario1")  \
		      .setMaster("spark://10.254.0.157:7077") \
		      .set("spark.eventLog.enabled", "true") \
		      .set("spark.eventLog.dir", "hdfs://10.254.0.157/eventLog")

    sc = SparkContext(conf=conf)
    spark = SparkSession(sc)

    # Loads input file. Partition based on location. The first item returned becomes the key automatically.
    tweets = sc.textFile(sys.argv[1], 20).map(lambda tweet: parseTweetsThruLocation(tweet))
    #print("\n\nKEYS: ", tweets.keys().collect(), "\n\n")
    #print("\n\nVALUES: ", tweets.values().collect(), "\n\n")
    tweets = tweets.distinct().groupByKey()

    # Write output using DataFrame, using Parquet format (the only option available for pyspark 2.0.0)
    tweetsDF = tweets.toDF(["country", "text"])
    #tweetsDF.write.mode('overwrite').partitionBy("country").parquet(sys.argv[2])
    tweetsDF.write.save(sys.argv[2], "parquet", "overwrite", "country")
    sc.stop()
