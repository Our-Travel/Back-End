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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatMessageService {

    private final MemberService memberService;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomService chatRoomService;
    private final ChatRoomAndChatMessageRepository chatRoomAndChatMessageRepository;

    @Transactional
    public void sendMessage(MessageRequest messageRequest, Long roomId, Long memberId) {
        Member member = memberService.findByMemberId(memberId);
        ChatMessage chatMessage = ChatMessage.of(messageRequest.getMessage(), member);
        ChatRoom chatRoom = chatRoomService.findByChatRoomId(roomId);
        ChatRoomAndChatMessage chatRoomAndChatMessage = ChatRoomAndChatMessage.of(chatRoom, chatMessage);
        chatMessageRepository.save(chatMessage);
        chatRoomAndChatMessageRepository.save(chatRoomAndChatMessage);
    }
}
