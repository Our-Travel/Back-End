package com.example.ot.app.chat.controller;

import com.example.ot.app.chat.dto.response.ShowChatRoomResponse;
import com.example.ot.app.chat.dto.response.ShowMyChatRoomsResponse;
import com.example.ot.app.chat.service.ChatRoomService;
import com.example.ot.base.rsData.RsData;
import com.example.ot.config.security.entity.MemberContext;
import com.example.ot.util.Util;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.ot.app.chat.code.ChatSuccessCode.*;

@Tag(name = "채팅방")
@RequiredArgsConstructor
@RestController
@RequestMapping("/room")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @Operation(summary = "채팅방 조회", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("{roomId}")
    public ResponseEntity<RsData> showChatRoom(@PathVariable Long roomId,
                                               @AuthenticationPrincipal MemberContext memberContext){
        ShowChatRoomResponse showChatRoomResponseList = chatRoomService.getChatRoom(roomId, memberContext.getId());
        return Util.spring.responseEntityOf(RsData.success(CHAT_ROOM_ENTERED, showChatRoomResponseList));
    }

//    @Operation(summary = "채팅 목록 조회", security = @SecurityRequirement(name = "bearerAuth"))
//    @GetMapping
//    public ResponseEntity<RsData> showMyChatRooms(@AuthenticationPrincipal MemberContext memberContext){
//        List<ShowMyChatRoomsResponse> showMyChatRoomsResponseList = chatRoomService.getMyChatRooms(memberContext.getId());
//        return Util.spring.responseEntityOf(RsData.success(CHAT_ROOM_LIST, showMyChatRoomsResponseList));
//    }
}
