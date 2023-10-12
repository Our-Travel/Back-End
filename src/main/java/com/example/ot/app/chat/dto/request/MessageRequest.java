package com.example.ot.app.chat.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class MessageRequest {

    private Long roomId;
    private String message;
    private String writerNickname;
    private String createdDate;

    public static MessageRequest CreateEnterMessage(Long roomId, String noticeMessage){
        return new MessageRequest(roomId, noticeMessage, "admin", String.valueOf(LocalDateTime.now()));
    }

    public static MessageRequest CreateExitMessage(Long roomId, String noticeMessage){
        return new MessageRequest(roomId, noticeMessage, "admin", String.valueOf(LocalDateTime.now()));
    }
}
