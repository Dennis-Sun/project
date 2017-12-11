#!/bin/bash
#hadoop fs -rm -R /project/output
#hadoop fs -mkdir /project/output
hadoop fs -rm -R /project/output/a1
rm intermediate/*
rm stats

dev=/tmp/dev
diskstats=/tmp/diskstats
size=$1

vm=vm-25-2
ssh -t $vm "sudo sh -c 'sync; echo 3 > /proc/sys/vm/drop_caches; cat /proc/net/dev > $dev; cat /proc/diskstats > $diskstats'"
scp $vm:$dev ~/project/intermediate/before_dev_vm2
scp $vm:$diskstats ~/project/intermediate/before_disk_vm2
receive1_2=`grep eth0 ~/project/intermediate/before_dev_vm2 | awk '{print $2}'`
transmit1_2=`grep eth0 ~/project/intermediate/before_dev_vm2 | awk '{print $10}'`
read1_2=`grep vda1 ~/project/intermediate/before_disk_vm2 | awk '{print $6}'`
write1_2=`grep vda1 ~/project/intermediate/before_disk_vm2 | awk '{print $10}'`

vm=vm-25-3
ssh -t $vm "sudo sh -c 'sync; echo 3 > /proc/sys/vm/drop_caches; cat /proc/net/dev > $dev; cat /proc/diskstats > $diskstats'"
scp $vm:$dev ~/project/intermediate/before_dev_vm3
scp $vm:$diskstats ~/project/intermediate/before_disk_vm3
receive1_3=`grep eth0 ~/project/intermediate/before_dev_vm3 | awk '{print $2}'`
transmit1_3=`grep eth0 ~/project/intermediate/before_dev_vm3 | awk '{print $10}'`
read1_3=`grep vda1 ~/project/intermediate/before_disk_vm3 | awk '{print $6}'`
write1_3=`grep vda1 ~/project/intermediate/before_disk_vm3 | awk '{print $10}'`

vm=vm-25-4
ssh -t $vm "sudo sh -c 'sync; echo 3 > /proc/sys/vm/drop_caches; cat /proc/net/dev > $dev; cat /proc/diskstats > $diskstats'"
scp $vm:$dev ~/project/intermediate/before_dev_vm4
scp $vm:$diskstats ~/project/intermediate/before_disk_vm4
receive1_4=`grep eth0 ~/project/intermediate/before_dev_vm4 | awk '{print $2}'`
transmit1_4=`grep eth0 ~/project/intermediate/before_dev_vm4 | awk '{print $10}'`
read1_4=`grep vda1 ~/project/intermediate/before_disk_vm4 | awk '{print $6}'`
write1_4=`grep vda1 ~/project/intermediate/before_disk_vm4 | awk '{print $10}'`

vm=vm-25-5
ssh -t $vm "sudo sh -c 'sync; echo 3 > /proc/sys/vm/drop_caches; cat /proc/net/dev > $dev; cat /proc/diskstats > $diskstats'"
scp $vm:$dev ~/project/intermediate/before_dev_vm5
scp $vm:$diskstats ~/project/intermediate/before_disk_vm5
receive1_5=`grep eth0 ~/project/intermediate/before_dev_vm5 | awk '{print $2}'`
transmit1_5=`grep eth0 ~/project/intermediate/before_dev_vm5 | awk '{print $10}'`
read1_5=`grep vda1 ~/project/intermediate/before_disk_vm5 | awk '{print $6}'`
write1_5=`grep vda1 ~/project/intermediate/before_disk_vm5 | awk '{print $10}'`

sudo sh -c "sync; echo 3 > /proc/sys/vm/drop_caches"
receive1=`grep eth0 /proc/net/dev | awk '{print $2}'`
transmit1=`grep eth0 /proc/net/dev | awk '{print $10}'`
read1=`grep vda1 /proc/diskstats | awk '{print $6}'`
write1=`grep vda1 /proc/diskstats | awk '{print $10}'`

start=$(date +%s)
spark-submit ~/project/PartA_1.py /project/tmp/tmp_$size /project/output/a1
#spark-submit ~/project/PartA_1.py /project/test/*.txt /project/output
#spark-submit ~/project/PartA_1.py /project/tmp/tmp_1g /project/output
#spark-submit ~/project/PartA_1.py /project/tmp/tmp_2g /project/output
#spark-submit ~/project/PartA_1.py /project/tmp/tmp_4g /project/output
#spark-submit ~/project/PartA_1.py /project/tmp/tmp_8g /project/output
#spark-submit ~/project/PartA_1.py /project/tmp/tmp_10g /project/output
#spark-submit ~/project/PartA_1.py /project/tmp/tmp_20g /project/output
#spark-submit ~/project/PartA_1.py /project/tmp/tmp_40g /project/output
#spark-submit ~/project/PartA_1.py /project/tmp/tmp_80g /project/output
end=$(date +%s)

receive2=`grep eth0 /proc/net/dev | awk '{print $2}'`
transmit2=`grep eth0 /proc/net/dev | awk '{print $10}'`
read2=`grep vda1 /proc/diskstats | awk '{print $6}'`
write2=`grep vda1 /proc/diskstats | awk '{print $10}'`

vm=vm-25-2
ssh -t $vm "cat /proc/net/dev > $dev; cat /proc/diskstats > $diskstats"
scp $vm:$dev ~/project/intermediate/dev_vm2
scp $vm:$diskstats ~/project/intermediate/disk_vm2
receive2_2=`grep eth0 ~/project/intermediate/dev_vm2 | awk '{print $2}'`
transmit2_2=`grep eth0 ~/project/intermediate/dev_vm2 | awk '{print $10}'`
read2_2=`grep vda1 ~/project/intermediate/disk_vm2 | awk '{print $6}'`
write2_2=`grep vda1 ~/project/intermediate/disk_vm2 | awk '{print $10}'`

vm=vm-25-3
ssh -t $vm "cat /proc/net/dev > $dev; cat /proc/diskstats > $diskstats"
scp $vm:$dev ~/project/intermediate/dev_vm3
scp $vm:$diskstats ~/project/intermediate/disk_vm3
receive2_3=`grep eth0 ~/project/intermediate/dev_vm3 | awk '{print $2}'`
transmit2_3=`grep eth0 ~/project/intermediate/dev_vm3 | awk '{print $10}'`
read2_3=`grep vda1 ~/project/intermediate/disk_vm3 | awk '{print $6}'`
write2_3=`grep vda1 ~/project/intermediate/disk_vm3 | awk '{print $10}'`

vm=vm-25-4
ssh -t $vm "cat /proc/net/dev > $dev; cat /proc/diskstats > $diskstats"
scp $vm:$dev ~/project/intermediate/dev_vm4
scp $vm:$diskstats ~/project/intermediate/disk_vm4
receive2_4=`grep eth0 ~/project/intermediate/dev_vm4 | awk '{print $2}'`
transmit2_4=`grep eth0 ~/project/intermediate/dev_vm4 | awk '{print $10}'`
read2_4=`grep vda1 ~/project/intermediate/disk_vm4 | awk '{print $6}'`
write2_4=`grep vda1 ~/project/intermediate/disk_vm4 | awk '{print $10}'`

vm=vm-25-5
ssh -t $vm "cat /proc/net/dev > $dev; cat /proc/diskstats > $diskstats"
scp $vm:$dev ~/project/intermediate/dev_vm5
scp $vm:$diskstats ~/project/intermediate/disk_vm5
receive2_5=`grep eth0 ~/project/intermediate/dev_vm5 | awk '{print $2}'`
transmit2_5=`grep eth0 ~/project/intermediate/dev_vm5 | awk '{print $10}'`
read2_5=`grep vda1 ~/project/intermediate/disk_vm5 | awk '{print $6}'`
write2_5=`grep vda1 ~/project/intermediate/disk_vm5 | awk '{print $10}'`

echo 'receive = '$(($receive2+$receive2_2+$receive2_3+$receive2_4+$receive2_5-$receive1-$receive1_2-$receive1_3-$receive1_4-$receive1_5)) >> ~/project/stats
echo 'transmit = '$(($transmit2+$transmit2_2+$transmit2_3+$transmit2_4+$transmit2_5-$transmit1-$transmit1_2-$transmit1_3-$transmit1_4-$transmit1_5)) >> ~/project/stats
echo 'read = '$(($read2+$read2_2+$read2_3+$read2_4+$read2_5-$read1-$read1_2-$read1_3-$read1_4-$read1_5)) >> ~/project/stats
echo 'write = '$(($write2+$write2_2+$write2_3+$write2_4+$write2_5-$write1-$write1_2-$write1_3-$write1_4-$write1_5)) >> ~/project/stats
echo 'time = '$(($end-$start)) >> ~/project/stats
