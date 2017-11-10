from __future__ import print_function

import sys

from pyspark import SparkContext, SparkConf

def parseTweetsThruLocation(tweet):
    """Parses a line of tweet string."""
    parts = tweet.rsplit('|',3)[::-1]
    return parts[0], parts[2]

if __name__ == "__main__":
    if len(sys.argv) != 2:
        print >> sys.stderr, "Usage: PartA.py <inputpath>"
        exit(-1)

    # Initialize the spark context.
    conf = SparkConf().setAppName("Project-PartA-Scenario1")  \
		      .setMaster("spark://10.254.0.157:7077") \
		      .set("spark.eventLog.enabled", "true") \
		      .set("spark.eventLog.dir", "hdfs://10.254.0.157/eventLog")

    sc = SparkContext(conf=conf)

    # Loads in input file.
    tweets = sc.textFile(sys.argv[1]).map(lambda tweet: parseTweetsThruLocation(tweet))
    #print("\n\nKEYS: ", tweets.keys().collect(), "\n\n")
    #print("\n\nVALUES: ", tweets.values().collect(), "\n\n")
    tweets = tweets.distinct().groupByKey()
    sc.stop()
