package com.example.ot.app.chat.entity;

import com.example.ot.app.chat.dto.request.MessageRequest;
import com.example.ot.app.member.entity.Member;
import com.example.ot.base.entity.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE chat_message SET deleted_date = NOW() where id = ?")
@Where(clause = "deleted_date is NULL")
public class ChatMessage extends BaseTimeEntity {

    private String message;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member writer;

    public ChatMessage(MessageRequest messageRequest, Member member){
        this.message = messageRequest.getMessage();
        this.writer = member;
    }

    public ChatMessage(String message){
        this.message = message;
    }

    public static ChatMessage exitMessage(String exitMemberNickname) {
        return new ChatMessage("[알림]\n" + exitMemberNickname + " 님이 채팅방을 나가셨습니다.");
    }

    public static ChatMessage enterMessage(String enterMemberNickname) {
        return new ChatMessage("[알림]\n" + enterMemberNickname + " 님이 채팅방에 입장하셨습니다.");
    }
}
