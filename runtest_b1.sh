#!/bin/bash
hadoop fs -rm -R /project/output
hadoop fs -mkdir /project/output
spark-submit ~/project/PartB_1.py /project/test/*.txt /project/output
