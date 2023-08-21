package com.example.ot.app.chat.controller;

import com.example.ot.app.chat.dto.response.ChatRoomIdResponse;
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

    @Operation(summary = "채팅방 입장", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/{roomId}")
    public ResponseEntity<RsData> showChatRoom(@PathVariable Long roomId,
                                               @AuthenticationPrincipal MemberContext memberContext){
        ShowChatRoomResponse showChatRoomResponseList = chatRoomService.getChatRoom(roomId, memberContext.getId());
        return Util.spring.responseEntityOf(RsData.success(CHATROOM_ENTERED, showChatRoomResponseList));
    }


    @Operation(summary = "호스트 채팅방 생성", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/host/{hostMemberId}")
    public ResponseEntity<RsData> createHostChatRoom(@PathVariable Long hostMemberId,
                                               @AuthenticationPrincipal MemberContext memberContext){
        ChatRoomIdResponse chatRoomIdResponse = chatRoomService.createHostChatRoom(hostMemberId, memberContext.getId());
        return Util.spring.responseEntityOf(RsData.success(HOST_CHATROOM_CREATED, chatRoomIdResponse));
    }

    @Operation(summary = "채팅 목록 조회", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping
    public ResponseEntity<RsData> showMyChatRooms(@AuthenticationPrincipal MemberContext memberContext){
        List<ShowMyChatRoomsResponse> showMyChatRoomsResponseList = chatRoomService.getMyChatRooms(memberContext.getId());
        return Util.spring.responseEntityOf(RsData.success(CHATROOM_LIST, showMyChatRoomsResponseList));
    }

    @Operation(summary = "채팅방 나가기", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/{roomId}")
    public ResponseEntity<RsData> exitChatRoom(@PathVariable Long roomId,
                                                     @AuthenticationPrincipal MemberContext memberContext){
        chatRoomService.exitChatRoom(roomId, memberContext.getId());
        return Util.spring.responseEntityOf(RsData.success(CHATROOM_EXITED));
    }
}
