package goows.flink.server;

import goows.flink.server.kafka.KafkaSourceBuilder;
import goows.flink.server.util.KomoranTokenizer;
import goows.flink.server.util.Top5Sink;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;


public class App {
    public static void main(String[] args) throws Exception {

        // flink application entry point !_!
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // 카프카 토픽으로부터 데이터 구독
        // DataStream<String> text = env.fromElements();
        DataStream<String> input = env.addSource(KafkaSourceBuilder.create("news_search_topic"));

        // one-off to sink
        input
                .flatMap(new KomoranTokenizer())
                .keyBy(tuple -> tuple.f0)
                .sum(1)
                .addSink(new Top5Sink());
        env.execute("Top 5 Elements by Integer Value");
    }
}

