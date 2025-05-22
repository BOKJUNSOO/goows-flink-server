package goows.flink.server.kafka;


import io.github.cdimascio.dotenv.Dotenv;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

import java.util.Properties;

// consumer
public class KafkaSourceBuilder {
    public static FlinkKafkaConsumer<String> create(String topic) {
        Properties props = new Properties();
//        Dotenv dotenv = Dotenv.configure()
//                .directory("src/main/resources")
//                .load();
//        String kafkaUrl = dotenv.get("KAFKA_URL");
        props.setProperty("bootstrap.servers", "kafka:9092");
        props.setProperty("group.id", "flink-consumer-group");

        FlinkKafkaConsumer<String> consumer = new FlinkKafkaConsumer<>(
                topic,
                new SimpleStringSchema(),
                props
        );
        consumer.setStartFromEarliest();
        return consumer;
    }
}
