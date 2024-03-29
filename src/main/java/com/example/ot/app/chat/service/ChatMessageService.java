package com.example.ot.app.chat.service;

import com.example.ot.app.chat.dto.request.MessageRequest;
import com.example.ot.app.chat.entity.ChatMessage;
import com.example.ot.app.chat.entity.ChatRoom;
import com.example.ot.app.chat.entity.ChatRoomAndChatMessage;
import com.example.ot.app.chat.repository.ChatRoomAndChatMessageRepository;
import com.example.ot.app.chat.repository.ChatRoomRepository;
import com.example.ot.app.member.entity.Member;
import com.example.ot.app.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final MemberRepository memberRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomAndChatMessageRepository chatRoomAndChatMessageRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    public void sendMessage(MessageRequest messageRequest, Long memberId) {
        Member member = memberRepository.findByMemberId(memberId);
        ChatMessage chatMessage = ChatMessage.CreateMessageByMember(messageRequest, member);
        saveMessage(chatMessage, messageRequest.getRoomId());
    }

    @Transactional
    public void sendExitMessage(Long roomId, String exitMemberNickname) {
        ChatMessage chatMessage = ChatMessage.exitMessage(exitMemberNickname);
        saveMessage(chatMessage, roomId);
        MessageRequest noticeMessage = MessageRequest.CreateExitMessage(roomId, chatMessage.getMessage());
        messagingTemplate.convertAndSend("/sub/message/%d".formatted(roomId), noticeMessage);
    }

    public void sendEnterMessage(Long roomId, String enterMemberNickname) {
        ChatMessage chatMessage = ChatMessage.enterMessage(enterMemberNickname);
        saveMessage(chatMessage, roomId);
        MessageRequest noticeMessage = MessageRequest.CreateEnterMessage(roomId, chatMessage.getMessage());
        messagingTemplate.convertAndSend("/sub/message/%d".formatted(roomId), noticeMessage);
    }

    public void saveMessage(ChatMessage chatMessage, Long roomId){
        ChatRoom chatRoom = chatRoomRepository.findByChatRoomId(roomId);
        ChatRoomAndChatMessage chatRoomAndChatMessage = ChatRoomAndChatMessage.of(chatRoom, chatMessage);
        chatRoomAndChatMessageRepository.save(chatRoomAndChatMessage);
    }

}
