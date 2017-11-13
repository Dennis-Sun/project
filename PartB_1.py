from __future__ import print_function

import sys
from pyspark.sql import SparkSession
from pyspark.sql.types import StructType
from pyspark import SparkContext, SparkConf

if __name__ == "__main__":
    if len(sys.argv) != 3:
        print >> sys.stderr, "Usage: PartB_1.py <inputpath> <outputpath>"
        exit(-1)

    # Initialize the spark context.
    conf = SparkConf().setAppName("Project-PartB-Scenario1")  \
		      .setMaster("spark://10.254.0.157:7077") \
		      .set("spark.eventLog.enabled", "true") \
		      .set("spark.eventLog.dir", "hdfs://10.254.0.157/eventLog")

    sc = SparkContext(conf=conf)
    spark = SparkSession(sc)

    # Loads input file into a single DataFrame.
    input_path = sys.argv[1]
    output_path = sys.argv[2]

    userSchema = StructType().add("text", "string").add("timestamp", "string").add("country", "string")
    tweets = spark.read.csv(input_path, schema=userSchema, sep='|')
    #print("\n\nKEYS: ", tweets.keys().collect(), "\n\n")
    #print("\n\nVALUES: ", tweets.values().collect(), "\n\n")

    # Use SparkSQL to perform partitioning
    tweets.createOrReplaceTempView("table")
    tweets_country = spark.sql("SELECT country, text FROM table")
    #tweets_country.collect()

    # Write output using DataFrame, using Parquet format (the only option available for pyspark 2.0.0)
    tweets_country.write.save(sys.argv[2], "parquet", "overwrite", "country")
    sc.stop()
