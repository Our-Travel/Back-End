package com.example.ot.app.chat.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class MessageRequest {

    private Long roomId;
    private String message;
    private String writerNickname;

    public static MessageRequest CreateEnterMessage(Long roomId, String noticeMessage){
        return new MessageRequest(roomId, noticeMessage, "admin");
    }

    public static MessageRequest CreateExitMessage(Long roomId, String noticeMessage){
        return new MessageRequest(roomId, noticeMessage, "admin");
    }
}
