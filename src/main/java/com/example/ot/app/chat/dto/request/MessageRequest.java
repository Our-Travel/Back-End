package com.example.ot.app.chat.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class MessageRequest {

    private String message;
    private String writerNickname;

    public static MessageRequest notice(String noticeMessage) {
        return MessageRequest.builder()
                .message(noticeMessage)
                .writerNickname(null)
                .build();
    }
}
