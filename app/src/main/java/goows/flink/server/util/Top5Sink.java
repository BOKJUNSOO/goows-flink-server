package goows.flink.server.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.*;
import java.util.stream.Collectors;

public class Top5Sink extends RichSinkFunction<Tuple2<String, Integer>> {
    private final Map<String, Integer> counts = new HashMap<>();
    private transient ObjectMapper mapper;
    private transient KafkaProducer<String, String> producer;

    // open 은 초기설정 메서로 사용됨
    @Override
    public void open(org.apache.flink.configuration.Configuration parameters) {
        mapper = new ObjectMapper();

        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9093");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        
        // kafka 프로듀서 인스턴스 생성
        producer = new KafkaProducer<>(props);
    }

    @Override
    public void invoke(Tuple2<String, Integer> value, Context context) {
        counts.put(value.f0, value.f1);
    }

    // 실제 데이터 처리 로직 수행
    @Override
    public void close() throws Exception {
        List<Map<String, Object>> top5 = counts.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder()))
                .limit(5)
                .map(e -> {
                    Map<String, Object> obj = new LinkedHashMap<>();
                    obj.put("keyword", e.getKey());
                    obj.put("count", e.getValue());
                    return obj;
                })
                .collect(Collectors.toList());

        Map<String, Object> message = new LinkedHashMap<>();
        message.put("timestamp", System.currentTimeMillis());
        message.put("topKeywords", top5);

        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(message);

        // producing message !_!
        // top5-keywords 토픽으로 메세지 발행
        ProducerRecord<String, String> record = new ProducerRecord<>("top5-keywords", json);
        producer.send(record);

        producer.flush();
        if (producer != null) {
            producer.close();
        }
//        System.out.println("=== Kafka Message ===");
//        System.out.println(json);

    }
}
