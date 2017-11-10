#!/bin/bash
#spark-submit ~/project/PartA.py /project/small_processed
hadoop fs -rm -R /project/output
hadoop fs -mkdir /project/output
spark-submit ~/project/PartA_2.py /project/test/*.txt /project/output
