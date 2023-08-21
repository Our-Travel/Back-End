package com.example.ot.app.chat.dto;

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
public class ChatRoomDto {

    private Long roomId;
    private String roomTitle;
    private String writer;
    private String latestMessage;
    private LocalDateTime latestMessageTime;
    private boolean readAt;
}
