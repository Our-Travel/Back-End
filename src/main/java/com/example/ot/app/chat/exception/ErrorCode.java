package com.example.ot.app.chat.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    CHATROOM_NOT_EXISTS("존재하지 않는 채팅방입니다."),
    CHATROOM_ACCESS_UNAUTHORIZED("채팅방에 접근 권한이 없습니다."),
    CHATROOM_FULL("채팅방 인원이 가득 찼습니다."),
    CHATROOM_ENTRY_PERIOD_EXPIRED("채팅방 입장 기간이 아닙니다."),
    CHATROOM_HOST_CANNOT_CREATE_OWN("호스트는 자신의 채팅방을 생성할 수 없습니다."),
    CHATROOM_CANNOT_EXISTED("작성자는 남은인원이 있으면 채팅방에 나갈 수 없습니다.");

    ErrorCode(String msg){
        this.msg = msg;
    }

    private String msg;
}
