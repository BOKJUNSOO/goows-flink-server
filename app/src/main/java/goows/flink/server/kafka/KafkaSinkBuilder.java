package goows.flink.server.kafka;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;

// producer
public class KafkaSinkBuilder {
    public static FlinkKafkaProducer<String> create(String topic) {
        return new FlinkKafkaProducer<>(
                "kafka:9093",
                topic,
                new SimpleStringSchema()
        );
    }
}