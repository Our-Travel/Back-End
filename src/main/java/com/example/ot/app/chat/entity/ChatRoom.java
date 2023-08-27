package com.example.ot.app.chat.entity;

import com.example.ot.app.board.entity.TravelBoard;
import com.example.ot.app.host.entity.Host;
import com.example.ot.app.member.entity.Member;
import com.example.ot.base.entity.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE chat_room SET deleted_date = NOW() where id = ?")
@Where(clause = "deleted_date is NULL")
public class ChatRoom extends BaseTimeEntity {

    private String title;

    @OneToOne(fetch = FetchType.LAZY)
    private TravelBoard travelBoard;

    @ManyToOne(fetch = FetchType.LAZY)
    private Host host;

    public ChatRoom(TravelBoard travelBoard){
        this.title = travelBoard.getTitle();
        this.travelBoard = travelBoard;
    }

    public ChatRoom(Host host, Member hostMember, Member userMember){
        this.title = hostMember.getNickName() + "호스트님과 " + userMember.getNickName() + "님의 채팅방";
        this.host = host;
    }

    public Long getBoardId(){
        return travelBoard.getId();
    }

    public Member getBoardWriter(){
        return travelBoard.getMember();
    }
}
