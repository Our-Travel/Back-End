package com.example.ot.app.chat.event;

import lombok.Getter;

@Getter
public class SendExitMessageEvent {

    private final Long roomId;
    private final String exitMemberNickname;

    public SendExitMessageEvent(Long roomId, String exitMemberNickname){
        this.roomId = roomId;
        this.exitMemberNickname = exitMemberNickname;
    }
}
