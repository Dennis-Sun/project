// credit: https://gist.github.com/ashrithr/5811266

import kafka.producer.Partitioner;
import kafka.utils.VerifiableProperties;

public class TweetPartitioner implements Partitioner<String> {
  public TweetPartitioner (VerifiableProperties props) {

  }

  // assume key is the whole line of tweet
  public int partition(String key, int a_numPartitions) {
    int partition = 0;
    // partition based on country code
    int offset = key.lastIndexOf('|');
    partition = key.substring(offset+1).hashCode() % a_numPartitions;
    return partition;

    // partition based on timestamp.minute
    // String[] section = key.split("\\|", -1)
    // String timestamp = section[1].split("\\:")
    // String minute = timestamp[1]
    // partition = Integer.parseInt(minute) % a_numPartitions;
    // return partition;
  }

}
