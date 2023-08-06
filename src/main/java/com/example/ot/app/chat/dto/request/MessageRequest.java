package com.example.ot.app.chat.dto.request;

import lombok.Getter;

@Getter
public class MessageRequest {

    private Long roomId;
    private String message;
    private Long writerId;
}
