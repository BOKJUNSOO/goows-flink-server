package goows.flink.server.util;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;

public class Refine {
    public void orderKeywords(DataStream<Tuple2<String,Integer>> tuple2DataStream){
        System.out.println("test");
    }
}
