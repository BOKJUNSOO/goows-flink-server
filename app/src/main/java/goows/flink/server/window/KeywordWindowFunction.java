package goows.flink.server.window;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.flink.streaming.api.functions.windowing.ProcessAllWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class KeywordWindowFunction extends ProcessAllWindowFunction<String, String, TimeWindow> {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;

    @Override
    public void process(Context context, Iterable<String> elements, Collector<String> out) throws Exception {
        Map<String, Long> counts = new HashMap<>();
        for (String keyword : elements) {
            counts.put(keyword, counts.getOrDefault(keyword, 0L) + 1);
        }

        // ISO 8601 포맷이용 (메세지 메타데이터)
        String timestamp = formatter.format(Instant.ofEpochMilli(context.window().getEnd()));

        // json format
        ObjectNode root = mapper.createObjectNode();
        root.put("timestamp", timestamp);

        ArrayNode topKeywords = mapper.createArrayNode();
        for (Map.Entry<String, Long> entry : counts.entrySet()) {
            ObjectNode keywordObj = mapper.createObjectNode();
            keywordObj.put("keyword", entry.getKey());
            keywordObj.put("count", entry.getValue());
            topKeywords.add(keywordObj);
        }
        root.set("top_keywords", topKeywords);

        // go sink !
        out.collect(mapper.writeValueAsString(root));
    }
}
