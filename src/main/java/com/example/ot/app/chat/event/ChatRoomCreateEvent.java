package com.example.ot.app.chat.event;

import com.example.ot.app.board.entity.TravelBoard;
import com.example.ot.app.member.entity.Member;
import lombok.Getter;

@Getter
public class ChatRoomCreateEvent {

    private final TravelBoard travelBoard;
    private final Member member;

    public ChatRoomCreateEvent(TravelBoard travelBoard, Member member){
        this.travelBoard = travelBoard;
        this.member = member;
    }
}
