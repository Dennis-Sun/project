// credit: https://gist.github.com/ashrithr/5811266
//         https://www.javaworld.com/article/3060078/big-data/big-data-messaging-with-kafka-part-1.html

import java.util.*;

// import kafka.javaapi.producer.Producer;
import org.apache.kafka.clients.producer.Producer;
// import kafka.producer.KeyedMessage;
// import kafka.producer.ProducerConfig;
// import kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.KafkaProducer;

public class TweetProducer {
  public static void main(String[] args) {
    // long events = Long.parseLong(args[0]);
    // Random rnd = new Random();

    //Define properties for how the Producer finds the cluster, serializes
    //the messages and if appropriate directs the message to a specific
    //partition.
    Properties props = new Properties();
    // props.put("metadata.broker.list", "broker1:9092,broker2:9092");
    props.put("bootstrap.servers", "localhost:9092");
    props.put("key.serializer", "org.apache.kafka.common.serialization.LongSerializer");
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    props.put("partitioner.class", "TweetPartitioner");
    props.put("acks", "1");

    // ProducerConfig config = new ProducerConfig(props);

    //Define producer object, its a java generic and takes 2 params; first
    //type of partition key, second type of the message
    Producer<String, String> producer = new KafkaProducer<>(config);

    // Parse Static Data File & Topic Name
    filedir = args[0];
    topicName = args[1];
    try(BufferedReader br = new BufferedReader(new FileReader(filedir))) {
      // long i = 0;
      for(String line; (line = br.readLine()) != null; ) {
        // String[] tweet = line.split("\\|", -1);
        // String country = tweet[2];
        // String content = tweet[0];
        //Finally write the message to broker (here, page_visits is topic
        //name to write to, ip is the partition key and msg is the actual
        //message)
        // i += 1;
        // KeyedMessage<long, String> tweet = new KeyedMessage<long, String>(args[1], i, line);
        ProducerRecord<String, String> tweet = new ProducerRecord<String, String>(topicName, line);
        producer.send(tweet);
      }
    }
  }
  producer.close();
}
}
