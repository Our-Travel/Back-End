package com.example.ot.app.chat.dto;

import com.example.ot.app.chat.entity.ChatMessage;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ChatRoomMessageDto {

    private Long memberId;
    private String nickname;
    private String message;
    private String createdDate;

    public static ChatRoomMessageDto fromChatMessage(ChatMessage chatMessage) {
        return ChatRoomMessageDto.builder()
                .memberId(chatMessage.getWriter().getId())
                .nickname(chatMessage.getWriter().getNickName())
                .message(chatMessage.getMessage())
                .createdDate(String.valueOf(chatMessage.getCreatedDate()))
                .build();
    }
}
