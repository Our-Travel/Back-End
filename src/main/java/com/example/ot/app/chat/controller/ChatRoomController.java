package com.example.ot.app.chat.controller;

import com.example.ot.app.chat.service.ChatRoomService;
import com.example.ot.base.rsData.RsData;
import com.example.ot.config.security.entity.MemberContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "채팅")
@RequiredArgsConstructor
@RestController
@RequestMapping("/room")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

//    @Operation(summary = "나의 채팅방들 조회", security = @SecurityRequirement(name = "bearerAuth"))
//    @GetMapping
//    public ResponseEntity<RsData> showMyChatRoom(@AuthenticationPrincipal MemberContext memberContext){
//
//    }
}
