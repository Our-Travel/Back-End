package com.example.ot.app.chat.controller;

import com.example.ot.app.chat.dto.request.MessageRequest;
import com.example.ot.app.chat.service.ChatMessageService;
import com.example.ot.config.security.entity.MemberContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "채팅 메시지")
@RequiredArgsConstructor
@RestController
@RequestMapping("/message")
public class ChatMessageController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatMessageService chatMessageService;

    @Operation(summary = "채팅방 조회", security = @SecurityRequirement(name = "bearerAuth"))
    @MessageMapping
    public void sendMessage(@Payload MessageRequest messageRequest, @AuthenticationPrincipal MemberContext memberContext){
        chatMessageService.sendMessage(messageRequest, memberContext.getId());
        simpMessagingTemplate.convertAndSend("/sub/chat/room" + messageRequest.getRoomId(), messageRequest);
    }
}
