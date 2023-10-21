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
    private String profileImage;

    public static ChatRoomMessageDto fromChatMessage(ChatMessage chatMessage) {
        Member member = chatMessage.getWriter();
        String profileImage = null;
        if(!ObjectUtils.isEmpty(member)) {
            profileImage = (member.getProfileImage() == null) ? null : member.getProfileImage().getFullPath();
        }
        Long memberId = (member == null) ? null : member.getId();
        String nickname = (member == null) ? "admin" : member.getNickName();
        return ChatRoomMessageDto.builder()
                .memberId(memberId)
                .nickname(nickname)
                .message(chatMessage.getMessage())
                .profileImage(profileImage)
                .createdDate(String.valueOf(chatMessage.getCreatedDate()))
                .build();
    }
}
