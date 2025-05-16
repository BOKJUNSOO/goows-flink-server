package goows.flink.server.util;

import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import kr.co.shineware.nlp.komoran.model.Token;

import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.util.Collector;

import java.util.List;

public class KomoranTokenizer extends RichFlatMapFunction<String, Tuple2<String, Integer>> {

    private transient Komoran komoran;

    @Override
    public void open(Configuration parameters) {
        // FULL 로 작성하면 조금 더 무거운 모델
        komoran = new Komoran(DEFAULT_MODEL.LIGHT);
    }

    @Override
    public void flatMap(String sentence, Collector<Tuple2<String, Integer>> out) {
        KomoranResult result = komoran.analyze(sentence);
        List<Token> tokens = result.getTokenList();
        for (Token t : tokens) {
            String morph = t.getMorph();
            String pos   = t.getPos();
            // 일반명사(NNG), 고유명사(NNP)만 카운트
            if ((pos.equals("NNG") || pos.equals("NNP")) && morph.length() > 1) {
                out.collect(new Tuple2<>(morph, 1));
            }
        }
    }
}
