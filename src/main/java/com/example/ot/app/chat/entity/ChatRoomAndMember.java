package com.example.ot.app.chat.entity;

import com.example.ot.app.member.entity.Member;
import com.example.ot.base.entity.BaseTimeEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SQLDelete(sql = "UPDATE chat_room_and_member SET deleted_date = NOW() where id = ?")
@Where(clause = "deleted_date is NULL")
public class ChatRoomAndMember extends BaseTimeEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    public static ChatRoomAndMember of(ChatRoom chatRoom, Member member){
        return new ChatRoomAndMember(chatRoom, member);
    }
}
