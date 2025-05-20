package goows.flink.server.sink;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class TrendingRankSink extends RichSinkFunction<String> {
    private transient KafkaProducer<String, String> producer;

    @Override
    public void open(Configuration parameters) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producer = new KafkaProducer<>(props);
    }

    @Override
    public void invoke(String value, Context context){
        ProducerRecord<String, String> record = new ProducerRecord<>("trending-keywords", value);
        producer.send(record);
        System.out.println("[Sink] - trending topic Received : " + value);
    }

    @Override
    public void close(){
        if(producer != null) {
            producer.close();
        }
    }
}
