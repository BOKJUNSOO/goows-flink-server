package goows.flink.server;

import goows.flink.server.kafka.KafkaSourceBuilder;
import goows.flink.server.util.KomoranProcessor;
import goows.flink.server.util.Top5Sink;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;


public class App {
    public static void main(String[] args) throws Exception {

        // flink application entry point !_!
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // 카프카 토픽으로부터 데이터 구독
        DataStream<String> input = env.addSource(KafkaSourceBuilder.create("news_search_topic"));
        input.print();

        // one-off
        input
                .map(new KomoranProcessor())
                .addSink(new Top5Sink());

        env.execute("Single Top 5 keywords");
    }
}

