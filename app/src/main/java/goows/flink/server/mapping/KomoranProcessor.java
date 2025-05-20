package goows.flink.server.mapping;

import com.fasterxml.jackson.databind.ObjectMapper;
import goows.flink.server.dto.UserText;
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import kr.co.shineware.nlp.komoran.model.Token;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;

import java.util.*;
import java.util.stream.Collectors;

public class KomoranProcessor extends RichMapFunction<UserText, String> {
    private transient Komoran komoran;

    @Override
    public void open(Configuration parameters) {
        komoran = new Komoran(DEFAULT_MODEL.LIGHT);
    }

    @Override
    public String map(UserText value) {
        KomoranResult result = komoran.analyze(value.getDescription());
        List<Token> tokens = result.getTokenList();

        Map<String, Integer> counts = new HashMap<>();
        for (Token t : tokens) {
            String morph = t.getMorph();
            String pos   = t.getPos();
            if ((pos.equals("NNG") || pos.equals("NNP")) && morph.length() > 1) {
                counts.merge(morph, 1, Integer::sum);
            }
        }

        Map<String, Integer> top5 = counts.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder()))
                .limit(5)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));

        // consumer 에게 전달할 데이터
        try {
            List<String> keywords = new ArrayList<>(top5.keySet());
            Map<String, Object> sorting = new HashMap<>();
            sorting.put("memberId", value.getMemberId());
            sorting.put("top5", keywords);
            return new ObjectMapper().writeValueAsString(sorting);
        } catch (Exception e) {
            return "{}";
        }
    }
}





