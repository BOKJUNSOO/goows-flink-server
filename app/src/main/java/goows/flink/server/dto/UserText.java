package goows.flink.server.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserText {
    private String memberId;
    private String description;

    public UserText() {}

    public UserText(String memberId, String description) {
        this.memberId = memberId;
        this.description = description;
    }
}
