package com.example.ot.app.chat.dto;

import com.example.ot.app.chat.entity.ChatMessage;
import com.example.ot.app.member.entity.Member;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

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
        Long memberId = (chatMessage.getWriter() == null) ? null : chatMessage.getWriter().getId();
        String nickname = (chatMessage.getWriter() == null) ? "admin" : chatMessage.getWriter().getNickName();

        return ChatRoomMessageDto.builder()
                .memberId(memberId)
                .nickname(nickname)
                .message(chatMessage.getMessage())
                .createdDate(String.valueOf(chatMessage.getCreatedDate()))
                .build();
    }
}
