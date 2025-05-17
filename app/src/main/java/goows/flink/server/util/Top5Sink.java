package goows.flink.server.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

import java.util.*;
import java.util.stream.Collectors;

public class Top5Sink extends RichSinkFunction<Tuple2<String, Integer>> {
    private final Map<String, Integer> counts = new HashMap<>();
    private transient ObjectMapper mapper;

    @Override
    public void open(org.apache.flink.configuration.Configuration parameters) {
        mapper = new ObjectMapper();
    }

    @Override
    public void invoke(Tuple2<String, Integer> value, Context context) {
        counts.put(value.f0, value.f1);
    }

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

        // 추후에 kafka 로 메세지 발행
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(message);
        System.out.println("=== Kafka Message ===");
        System.out.println(json);
    }
}
