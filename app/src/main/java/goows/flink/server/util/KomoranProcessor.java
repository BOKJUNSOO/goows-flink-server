package goows.flink.server.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import kr.co.shineware.nlp.komoran.model.Token;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;

import java.util.*;
import java.util.stream.Collectors;

public class KomoranProcessor extends RichMapFunction<String, String> {
    private transient Komoran komoran;

    @Override
    public void open(Configuration parameters) {
        komoran = new Komoran(DEFAULT_MODEL.LIGHT);
    }

    @Override
    public String map(String value) {
        KomoranResult result = komoran.analyze(value);
        List<Token> tokens = result.getTokenList();

        Map<String, Integer> counts = new HashMap<>();
        for (Token t : tokens) {
            String morph = t.getMorph();
            String pos   = t.getPos();
            if ((pos.equals("NNG") || pos.equals("NNP")) && morph.length() > 1) {
                counts.merge(morph, 1, Integer::sum);
            }
        }

        // top 5 키워드 추출
        Map<String, Integer> top5 = counts.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder()))
                .limit(5)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));

        // JSON 직렬화
        try {
            return new ObjectMapper().writeValueAsString(top5);
        } catch (Exception e) {
            return "{}";  // fallback
        }
    }
}




