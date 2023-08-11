package com.example.ot.app.chat.exception;

public class ChatException extends RuntimeException {
    public ChatException(ErrorCode errorCode){
        super(errorCode.getMsg());
    }
}
