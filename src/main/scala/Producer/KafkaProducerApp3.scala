package Producer

// https://dzone.com/articles/custom-partitioner-in-kafka-using-scala-lets-take

import java.util.Properties
import org.apache.kafka.clients.producer._

object KafkaProducerApp3 extends App {

  val props = new Properties()
  val topicName = "department"
  props.put("bootstrap.servers", "localhost:9092,localhost:9093")
  props.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer")
  props.put("value.serializer","org.apache.kafka.common.serialization.StringSerializer")
  //props.put("partitioner.class", "com.Partitioner.CustomPartitioner")

  val producer = new KafkaProducer[String, String](props)

  try {
    for (i <- 0 to 5) {
      val record = new ProducerRecord[String, String](topicName,"IT" + i,"My Site is yen.com " + i)
      println(record)
      producer.send(record)
    }

    for (i <- 0 to 5) {
      val record = new ProducerRecord[String, String](topicName,"COMP" + i,"My Site is yen.com " + i)
      println(record)
      producer.send(record)
    }

  } catch {
    case e: Exception => e.printStackTrace()
  } finally {
    producer.close()
  }
}