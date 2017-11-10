#!/bin/bash
hadoop fs -rm -R /project/output
hadoop fs -mkdir /project/output
spark-submit ~/project/PartA_1.py /project/test/*.txt /project/output
