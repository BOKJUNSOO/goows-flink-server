package goows.flink.server;

import com.fasterxml.jackson.databind.ObjectMapper;

import goows.flink.server.dto.NewsMessage;
import goows.flink.server.dto.UserText;
import goows.flink.server.mapping.KomoranProcessor;
import goows.flink.server.sink.Top5Sink;
import goows.flink.server.kafka.KafkaSourceBuilder;

import goows.flink.server.sink.TrendingRankSink;
import goows.flink.server.window.KeywordWindowFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.SlidingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.triggers.ContinuousProcessingTimeTrigger;


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

        // 키워드 파싱 및 집계를 위한 전처리
        // keyWord 파싱
        DataStream<String> streamKeyword = parsed.map(NewsMessage::getKeyword);
        // sink 에 넣을 참조변수 추가
        DataStream<String> keywordCountJson = streamKeyword
                .windowAll(SlidingProcessingTimeWindows.of(Time.minutes(1), Time.seconds(10)))
                .trigger(ContinuousProcessingTimeTrigger.of(Time.seconds(10)))
                .process(new KeywordWindowFunction());

        // one-off sink!
        // top5-keywords topic
        enrichedDescriptions
                .map(new KomoranProcessor())
                .addSink(new Top5Sink());

        // stream ranking windows sink!
        // trending-keywords topic
        keywordCountJson
                .addSink(new TrendingRankSink());

        //execute flink job
        env.execute("Single Top 5 keywords");
    }
}

