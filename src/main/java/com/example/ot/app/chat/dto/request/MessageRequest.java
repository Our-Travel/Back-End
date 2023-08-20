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

    private Long roomId;
    private String message;
    private String writerNickname;

    public static MessageRequest notice(Long roomId, String noticeMessage) {
        return MessageRequest.builder()
                .roomId(roomId)
                .message(noticeMessage)
                .writerNickname(null)
                .build();
    }
}
