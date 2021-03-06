package KafkaCourse;

// https://www.youtube.com/watch?v=FybZ9gMFytE&list=PLmOn9nNkQxJEDjzl0iBYZ3WuXUuUStxZl&index=10
// create a producer via kafka Java high level API

import java.util.Properties;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

public class ProducerHighLevelAPI {
    public static void main(String[] args) throws Exception{
        System.out.println("ProducerHighLevelAPI run ...");

        // 0) Config
        // good to check the default config : /kafka/config/producer.Properties
        Properties props = new Properties();

        // kafka server name and port
        // could be "hadoop1:9092, hadoop2:9092, ...." if is a cluster
        props.setProperty("bootstrap.servers", "localhost:9092");

        // serialize the key, value object (msg) for better transmission performance (optional)
        props.setProperty("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.setProperty("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        // wait till all followers' (copy) responses (optional)
        // could be ["all", -1, 0 , 1]
        props.setProperty("acks", "1");

        // add partition (optional)
        props.setProperty("KafkaCourse.class", "KafkaCourse.MyPartitioner");

        // max retry when sending msg (optional)
        props.put("retries", 0);

        // size of one batch of msg (optional)
        props.put("batch.size", 16384);

        // 1) Create producer
        // Producer<key, value> -> the msg will be in the <key, value > format
        // while key is relative to the msg partition
        Producer<String, String> producer = new KafkaProducer<String, String>(props);

        // 2) Prepare the data (msg)
        String topic = "fitst_2";
        String value = "hello kafka !!!!!!";
        ProducerRecord record = new ProducerRecord(topic, value);
        // or can specify partition value here
        //ProducerRecord record = new ProducerRecord(topic, 1, null, value);


        // 3) Send the msg (Asynchronous)
        // https://github.com/yennanliu/JavaHelloWorld/blob/main/src/main/java/thread/CallableThreadDemo_1.java
        //producer.send(record);

        // 3') Send the msg (Synchronize)
        // producer.send(record).get();

        // 3'') Send the msg (Asynchronous) and wait for call back
        producer.send(record, new Callback() {
            // call back method
            public void onCompletion(RecordMetadata metadata, Exception exception) {
                // to which partition
                System.out.println("*** partition : " + metadata.partition());
                // offset
                System.out.println("*** offset : " +  metadata.offset());
            }
        });

        // close the producer
        producer.close();
    }
}
