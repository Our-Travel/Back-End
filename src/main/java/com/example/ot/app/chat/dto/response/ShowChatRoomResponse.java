package com.example.ot.app.chat.dto.response;

import com.example.ot.app.chat.dto.ChatRoomMessageDto;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ShowChatRoomResponse {

    private Long myMemberId;
    private Long chatRoomId;
    private List<ChatRoomMessageDto> chatRoomMessageDtoList;
}
