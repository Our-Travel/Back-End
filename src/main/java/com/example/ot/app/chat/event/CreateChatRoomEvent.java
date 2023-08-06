package com.example.ot.app.chat.event;

import com.example.ot.app.board.entity.TravelBoard;
import com.example.ot.app.member.entity.Member;
import lombok.Getter;

@Getter
public class CreateChatRoomEvent {

    private final TravelBoard travelBoard;
    private final Member member;

    public CreateChatRoomEvent(TravelBoard travelBoard, Member member){
        this.travelBoard = travelBoard;
        this.member = member;
    }
}
