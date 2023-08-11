package com.example.ot.app.chat.code;

import com.example.ot.base.code.Code;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ChatSuccessCode implements Code {
    CHAT_ROOM_ENTERED("채팅방에 입장하였습니다."),
    CHAT_ROOM_LIST("채팅 목록을 불러옵니다.");

    private String message;
}
