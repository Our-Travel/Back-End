package com.example.ot.app.chat.controller;

import com.example.ot.app.chat.dto.request.MessageRequest;
import com.example.ot.app.chat.service.ChatMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "채팅 메시지")
@RequiredArgsConstructor
@RestController
public class ChatMessageController {

    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Operation(summary = "채팅 메시지 전송")
    @MessageMapping("/message")
    public void sendMessage(MessageRequest messageRequest, @Header(value = "memberId", required = false) Long memberId){
        chatMessageService.sendMessage(messageRequest, memberId);
        simpMessagingTemplate.convertAndSend("/sub/message/" + messageRequest.getRoomId(), messageRequest);
    }
}
