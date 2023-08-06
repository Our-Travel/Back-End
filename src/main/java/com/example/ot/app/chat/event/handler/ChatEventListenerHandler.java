package com.example.ot.app.chat.event.handler;

import com.example.ot.app.chat.event.ChatRoomCreateEvent;
import com.example.ot.app.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatEventListenerHandler {

    private final ChatRoomService chatRoomService;

    @EventListener
    public void ChatRoomCrateEventListener(ChatRoomCreateEvent event){
        chatRoomService.createChatRoomByTravelBoard(event.getTravelBoard(), event.getMember());
    }

}
