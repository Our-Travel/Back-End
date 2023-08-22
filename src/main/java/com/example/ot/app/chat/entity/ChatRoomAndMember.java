package com.example.ot.app.chat.entity;

import com.example.ot.app.member.entity.Member;
import com.example.ot.base.entity.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Getter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE chat_room_and_member SET deleted_date = NOW() where id = ?")
@Where(clause = "deleted_date is NULL")
public class ChatRoomAndMember extends BaseTimeEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private ChatRoom chatRoom;

    public static ChatRoomAndMember of(ChatRoom chatRoomByTravelBoard, Member member){
        return ChatRoomAndMember.builder()
                .member(member)
                .chatRoom(chatRoomByTravelBoard)
                .build();
    }
}