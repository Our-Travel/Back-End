package com.example.ot.app.chat.event.handler;

import com.example.ot.app.chat.event.CreateChatRoomEvent;
import com.example.ot.app.chat.event.SendEnterMessageEvent;
import com.example.ot.app.chat.event.SendExitMessageEvent;
import com.example.ot.app.chat.service.ChatMessageService;
import com.example.ot.app.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatEventListenerHandler {

    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;

    @EventListener
    public void CrateChatRoomEventListener(CreateChatRoomEvent event){
        chatRoomService.createChatRoomByTravelBoard(event.getTravelBoard(), event.getMember());
    }

    @EventListener
    public void SendExitMessageEventListener(SendExitMessageEvent event){
        chatMessageService.sendExitMessage(event.getRoomId(), event.getExitMemberNickname());
    }

    public void SendEnterMessageEventListener(SendEnterMessageEvent event){
        chatMessageService.sendEnterMessage(event.getRoomId(), event.getEnterMemberNickname());
    }

}
