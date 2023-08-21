package com.example.ot.app.chat.service;

import com.example.ot.app.chat.dto.request.MessageRequest;
import com.example.ot.app.chat.entity.ChatMessage;
import com.example.ot.app.chat.entity.ChatRoom;
import com.example.ot.app.chat.entity.ChatRoomAndChatMessage;
import com.example.ot.app.chat.repository.ChatMessageRepository;
import com.example.ot.app.chat.repository.ChatRoomAndChatMessageRepository;
import com.example.ot.app.member.entity.Member;
import com.example.ot.app.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final MemberService memberService;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomService chatRoomService;
    private final ChatRoomAndChatMessageRepository chatRoomAndChatMessageRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    public void sendMessage(MessageRequest messageRequest, Long memberId) {
        Member member = memberService.findByMemberId(memberId);
        ChatMessage chatMessage = ChatMessage.of(messageRequest.getMessage(), member);
        saveMessage(chatMessage, messageRequest.getRoomId());
    }

    public void sendExitMessage(Long roomId, String exitMemberNickname) {
        ChatMessage chatMessage = ChatMessage.exitMessage(exitMemberNickname);
        saveMessage(chatMessage, roomId);
        MessageRequest noticeMessage = MessageRequest.notice(roomId, chatMessage.getMessage());
        messagingTemplate.convertAndSend("/sub/room/%d".formatted(roomId), noticeMessage);
    }

    public void saveMessage(ChatMessage chatMessage, Long roomId){
        ChatRoom chatRoom = chatRoomService.findByChatRoomId(roomId);
        ChatRoomAndChatMessage chatRoomAndChatMessage = ChatRoomAndChatMessage.of(chatRoom, chatMessage);
        chatMessageRepository.save(chatMessage);
        chatRoomAndChatMessageRepository.save(chatRoomAndChatMessage);
    }


}
