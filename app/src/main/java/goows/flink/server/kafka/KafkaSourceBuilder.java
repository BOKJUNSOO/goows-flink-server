package goows.flink.server.kafka;


import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

import java.util.Properties;

// consumer
public class KafkaSourceBuilder {
    public static FlinkKafkaConsumer<String> create(String topic) {
        Properties props = new Properties();
        props.setProperty("bootstrap.servers", "localhost:9092");
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
