// credit: https://gist.github.com/ashrithr/5811266

import java.util.*;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

public class TweetProducer {
    public static void main(String[] args) {
        long events = Long.parseLong(args[0]);
        Random rnd = new Random();

        //Define properties for how the Producer finds the cluster, serializes
        //the messages and if appropriate directs the message to a specific
        //partition.
        Properties props = new Properties();
        props.put("metadata.broker.list", "broker1:9092,broker2:9092 ");
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("partitioner.class", "example.producer.TweetPartitioner");
        props.put("request.required.acks", "1");

        ProducerConfig config = new ProducerConfig(props);

        //Define producer object, its a java generic and takes 2 params; first
        //type of partition key, second type of the message
        Producer<String, String> producer = new Producer<String, String>(config);

        for (long nEvents = 0; nEvents < events; nEvents++) {
                //build message
               long runtime = new Date().getTime();
               String ip = “192.168.2.” + rnd.nextInt(255);
               String msg = runtime + “,www.example.com,” + ip;

               //Finally write the message to broker (here, page_visits is topic
               //name to write to, ip is the partition key and msg is the actual
               //message)
               KeyedMessage<String, String> data = new KeyedMessage<String, String>("page_visits", ip, msg);
               producer.send(data);
        }
        producer.close();
    }
}
