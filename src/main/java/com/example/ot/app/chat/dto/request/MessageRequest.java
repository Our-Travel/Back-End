package com.example.ot.app.chat.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Getter
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class MessageRequest {

    private Long roomId;
    private String message;
    private String writerNickname;

    public MessageRequest(Long roomId, String noticeMessage){
        this.roomId = roomId;
        this.message = noticeMessage;
    }
}
