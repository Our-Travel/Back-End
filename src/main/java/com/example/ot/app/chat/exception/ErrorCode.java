package com.example.ot.app.chat.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    WRITER_AND_MEMBER_MISMATCH("채팅을 보낸 유저와 로그인한 유저가 일치하지 않습니다."),
    CHATROOM_NOT_EXISTS("존재하지 않는 채팅방입니다.");

    ErrorCode(String msg){
        this.msg = msg;
    }

    private String msg;
}
