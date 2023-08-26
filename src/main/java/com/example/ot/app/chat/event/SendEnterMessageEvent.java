package com.example.ot.app.chat.event;

import lombok.Getter;

@Getter
public class SendEnterMessageEvent {

    private final Long roomId;
    private final String enterMemberNickname;

    public SendEnterMessageEvent(Long roomId, String enterMemberNickname){
        this.roomId = roomId;
        this.enterMemberNickname = enterMemberNickname;
    }
}
