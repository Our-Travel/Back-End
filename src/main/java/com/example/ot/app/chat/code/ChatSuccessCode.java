package com.example.ot.app.chat.code;

import com.example.ot.base.code.Code;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ChatSuccessCode implements Code {
    CHATROOM_ENTERED("채팅방에 입장하였습니다."),
    CHATROOM_LIST("채팅 목록을 불러옵니다."),
    HOST_CHATROOM_CREATED("채팅방을 생성하였습니다."),
    CHATROOM_EXITED("채팅방에 나가셨습니다.");

    private String message;
}
