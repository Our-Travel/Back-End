package com.example.ot.app.chat.dto.response;

import com.example.ot.app.chat.dto.ChatRoomDto;
import com.example.ot.app.chat.entity.ChatMessage;
import com.example.ot.app.chat.entity.ChatRoom;
import com.example.ot.app.member.entity.ProfileImage;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ShowMyChatRoomsResponse {

    private Long roomId;
    private Integer regionCode;
    private String roomTitle;
    private String writer;
    private String image;
    private String latestMessage;
    private String latestMessageTime;
    private String roomManager;
    private boolean readAt;

    public static ShowMyChatRoomsResponse of(ChatRoom chatRoom, List<ChatMessage> messages, ProfileImage profileImage) {
        String latestMessage;
        String writer = null;
        String image = null;
        Integer regionCode = null;
        String roomManager = null;

        if (ObjectUtils.isEmpty(messages)) {
            latestMessage = "새로운 채팅방이 생성되었습니다.";
        } else {
            ChatMessage chatMessage = messages.get(0);
            latestMessage = chatMessage.getMessage();
            writer = (chatMessage.getWriter() == null) ? "[알림]" : chatMessage.getWriter().getNickName();
        }
        if(!ObjectUtils.isEmpty(chatRoom.getHost()) && !ObjectUtils.isEmpty(profileImage)) {
            image = profileImage.getFullPath();
        } else if(!ObjectUtils.isEmpty(chatRoom.getTravelBoard())){
            regionCode = chatRoom.getTravelBoard().getRegionCode();
            roomManager = chatRoom.getTravelBoard().getMember().getNickName();
        }
        return ShowMyChatRoomsResponse.builder()
                .roomId(chatRoom.getId())
                .roomTitle(chatRoom.getTitle())
                .writer(writer)
                .image(image)
                .roomManager(roomManager)
                .regionCode(regionCode)
                .latestMessage(latestMessage)
                .latestMessageTime(ObjectUtils.isEmpty(messages) ? null : String.valueOf(messages.get(0).getCreatedDate()))
                .build();
    }
}
