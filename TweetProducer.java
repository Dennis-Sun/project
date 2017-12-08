// credit: https://gist.github.com/ashrithr/5811266

import java.util.*;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

public class TweetProducer {
  public static void main(String[] args) {
    // long events = Long.parseLong(args[0]);
    // Random rnd = new Random();

    //Define properties for how the Producer finds the cluster, serializes
    //the messages and if appropriate directs the message to a specific
    //partition.
    Properties props = new Properties();
    // props.put("metadata.broker.list", "broker1:9092,broker2:9092 ");
    props.put("serializer.class", "kafka.serializer.StringEncoder");
    props.put("partitioner.class", "example.producer.TweetPartitioner");
    // props.put("request.required.acks", "1");

    ProducerConfig config = new ProducerConfig(props);

    //Define producer object, its a java generic and takes 2 params; first
    //type of partition key, second type of the message
    Producer<String, String> producer = new Producer<String, String>(config);

    // Parse Static Data File
    filedir = args[0]
    try(BufferedReader br = new BufferedReader(new FileReader(filedir))) {
      long i = 0;
      for(String line; (line = br.readLine()) != null; ) {
        // String[] tweet = line.split("\\|", -1);
        // String country = tweet[2];
        // String content = tweet[0];
        //Finally write the message to broker (here, page_visits is topic
        //name to write to, ip is the partition key and msg is the actual
        //message)
        i += 1;
        KeyedMessage<long, String> tweet = new KeyedMessage<long, String>("unmodified_tweets", i, line);
        producer.send(data);
      }
    }
  }
  producer.close();
}
}
