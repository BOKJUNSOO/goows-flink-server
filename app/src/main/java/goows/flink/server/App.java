package goows.flink.server;

import com.fasterxml.jackson.databind.ObjectMapper;

import goows.flink.server.kafka.KafkaSourceBuilder;
import goows.flink.server.kafka.NewsMessage;
import goows.flink.server.kafka.UserText;
import goows.flink.server.util.KomoranProcessor;
import goows.flink.server.util.Top5Sink;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;



public class App {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws Exception {

        // flink application entry point !_!
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // 카프카 토픽으로부터 데이터 구독
        DataStream<String> input = env.addSource(KafkaSourceBuilder.create("news-search-topic"));


        // 데이터 전처리
        DataStream<NewsMessage> parsed = input
                .map(json -> new ObjectMapper()
                        .readValue(json, NewsMessage.class)
                );
        
        // 키워드 추천을 위한 전처리
        DataStream<UserText> enrichedDescriptions = parsed.map(dto -> {
            String descriptionsJson;
            try {
                descriptionsJson = mapper.writeValueAsString(dto.getNewsList());
            } catch (Exception e) {
                descriptionsJson = "[]";
            }
            // memberId, description 파싱
            return new UserText(dto.getMemberId(), descriptionsJson);
        });

        // TODO: kewory 파싱 및 실시간 스트리밍 집계처리
        // DataStream<Something new class in here> ~_~


        // one-off
        enrichedDescriptions
                .map(new KomoranProcessor())
                .addSink(new Top5Sink());
        
        // stream ranking windows
        // some sink code~

        env.execute("Single Top 5 keywords");
    }
}

