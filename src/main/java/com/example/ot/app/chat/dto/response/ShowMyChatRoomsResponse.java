package com.example.ot.app.chat.dto.response;

import com.example.ot.app.chat.dto.ChatRoomDto;
import com.example.ot.app.chat.entity.ChatMessage;
import com.example.ot.app.chat.entity.ChatRoom;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ShowMyChatRoomsResponse {

    private Long roomId;
    private String roomTitle;
    private String writer;
    private String latestMessage;
    private String latestMessageTime;
    private boolean readAt;

    public static ShowMyChatRoomsResponse of(ChatRoom chatRoom, ChatMessage chatMessage) {
        if(ObjectUtils.isEmpty(chatMessage)){
            return ShowMyChatRoomsResponse.builder()
                    .roomId(chatRoom.getId())
                    .roomTitle(chatRoom.getTitle())
                    .latestMessage("새로운 채팅방이 생성되었습니다.")
                    .build();
        }
        return ShowMyChatRoomsResponse.builder()
                .roomId(chatRoom.getId())
                .roomTitle(chatRoom.getTitle())
                .writer(chatMessage.getWriter().getNickName())
                .latestMessage(chatMessage.getMessage())
                .latestMessageTime(String.valueOf(chatMessage.getCreatedDate()))
                .build();
    }
}
