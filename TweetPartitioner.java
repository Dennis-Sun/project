// credit: https://gist.github.com/ashrithr/5811266

import kafka.producer.Partitioner;
import kafka.utils.VerifiableProperties;

public class TweetPartitioner implements Partitioner<String> {
    public TweetPartitioner (VerifiableProperties props) {

    }

    public int partition(String key, int a_numPartitions) {
        int partition = 0;
        int offset = key.lastIndexOf('.');
        if (offset > 0) {
           partition = Integer.parseInt( key.substring(offset+1)) % a_numPartitions;
        }
       return partition;
  }

}
