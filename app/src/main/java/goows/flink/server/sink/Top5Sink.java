package goows.flink.server.sink;

import io.github.cdimascio.dotenv.Dotenv;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.*;

public class Top5Sink extends RichSinkFunction<String> {
    private transient KafkaProducer<String, String> producer;

    @Override
    public void open(Configuration parameters) {
        Properties props = new Properties();
//        Dotenv dotenv = Dotenv.configure()
//                .directory("src/main/resources")
//                .load();
//        String kafkaUrl = dotenv.get("KAFKA_URL");
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producer = new KafkaProducer<>(props);
    }

    @Override
    public void invoke(String value, Context context) throws Exception {
        ProducerRecord<String, String> record = new ProducerRecord<>("top5-keywords", value);
        producer.send(record);
        System.out.println("[Sink] - top5-keywords topic Received : " + value);
    }

    @Override
    public void close() throws Exception {
        if(producer != null) {
            producer.close();
        }
    }
}



