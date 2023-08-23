package com.example.ot.app.chat.entity;

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
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SQLDelete(sql = "UPDATE chat_room_and_chat_message SET deleted_date = NOW() where id = ?")
@Where(clause = "deleted_date is NULL")
public class ChatRoomAndChatMessage extends BaseTimeEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    private ChatMessage chatMessage;

    public static ChatRoomAndChatMessage of(ChatRoom chatRoom, ChatMessage chatMessage) {
        return ChatRoomAndChatMessage.builder()
                .chatRoom(chatRoom)
                .chatMessage(chatMessage)
                .build();
    }
}
