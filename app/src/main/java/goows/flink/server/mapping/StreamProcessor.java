package goows.flink.server.mapping;

import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.util.Collector;

import java.util.Map;

// flink templete
// 함수형 프로그래밍에서
// chain String (IN)
// chain Map<T,T> (OUT)
public class StreamProcessor extends RichFlatMapFunction<String, Map<String,Integer>> {
    @Override
    public void open(Configuration parameters) {

    }

    @Override
    public void flatMap(String keyword, Collector<Map<String, Integer>> out) {

    }
    
    // sink 단계가 아니므로 out을 정의하지 않음
//    @Override
//    public void out () {
//        
//    }
}
