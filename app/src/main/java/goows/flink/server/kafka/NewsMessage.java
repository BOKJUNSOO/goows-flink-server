package goows.flink.server.kafka;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

// Data Source 단계의 역직렬화를 위한 클래스
// string 형태의 json 을 역직렬화
@Getter
@Setter
public class NewsSearchSendDataDto {

    private String memberId;

    private String keyword;

    private List<NewsItemDto> newsList;

    @Data
    public static class NewsItemDto {
        private String title;
        private String description;
        private String pubDate;
    }
}
