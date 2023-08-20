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
@SQLDelete(sql = "UPDATE chat_message SET deleted_date = NOW() where id = ?")
@Where(clause = "deleted_date is NULL")
public class ChatMessage extends BaseTimeEntity {

    private String message;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member writer;

    public static ChatMessage of(String message, Member member) {
        return ChatMessage.builder()
                .message(message)
                .writer(member)
                .build();
    }

    public static ChatMessage exitMessage(String exitMemberNickname) {
        return ChatMessage.builder()
                .message("[알림]\n" + exitMemberNickname + " 님이 채팅방을 나가셨습니다.")
                .build();
    }
}
