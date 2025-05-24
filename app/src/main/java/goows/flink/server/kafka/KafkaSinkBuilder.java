package goows.flink.server.kafka;

import io.github.cdimascio.dotenv.Dotenv;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;

import java.util.Properties;

// producer
public class KafkaSinkBuilder {
    public static FlinkKafkaProducer<String> create(String topic) {
        Properties props = new Properties();
        Dotenv dotenv = Dotenv.configure().directory("src/main/resources").load();
        String kafkaUrl = dotenv.get("KAFKA_URL");
        props.setProperty("bootstrap.servers",kafkaUrl);

        return new FlinkKafkaProducer<>(
                topic,
                new SimpleStringSchema(),
                props
        );
    }
}