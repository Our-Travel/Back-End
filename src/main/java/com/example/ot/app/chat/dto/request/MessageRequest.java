package com.example.ot.app.chat.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Getter
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class MessageRequest {

    private Long roomId;
    private String message;
    private String writerNickname;

    private MessageRequest(Long roomId, String noticeMessage){
        this.roomId = roomId;
        this.message = noticeMessage;
    }

    public static MessageRequest CreateEnterMessage(Long roomId, String noticeMessage){
        return new MessageRequest(roomId, noticeMessage);
    }

    public static MessageRequest CreateExitMessage(Long roomId, String noticeMessage){
        return new MessageRequest(roomId, noticeMessage);
    }
}
