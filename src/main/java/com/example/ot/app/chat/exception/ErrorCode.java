package com.example.ot.app.chat.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    CHATROOM_NOT_EXISTS("존재하지 않는 채팅방입니다."),
    CHATROOM_ACCESS_UNAUTHORIZED("접근 권한이 없습니다."),
    CHATROOM_FULL("채팅방 인원이 가득 찼습니다.");

    ErrorCode(String msg){
        this.msg = msg;
    }

    private String msg;
}
