package goows.flink.server.kafka;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;

import java.util.Properties;

// producer
public class KafkaSinkBuilder {
    public static FlinkKafkaProducer<String> create(String topic) {
        Properties props = new Properties();
        props.setProperty("bootstrap.servers","kafka:9092");

        return new FlinkKafkaProducer<>(
                topic,
                new SimpleStringSchema(),
                props
        );
    }
}