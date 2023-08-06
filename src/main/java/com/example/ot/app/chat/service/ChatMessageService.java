package com.example.ot.app.chat.service;

import com.example.ot.app.chat.dto.request.MessageRequest;
import com.example.ot.app.chat.entity.ChatMessage;
import com.example.ot.app.chat.entity.ChatRoom;
import com.example.ot.app.chat.entity.ChatRoomAndChatMessage;
import com.example.ot.app.chat.exception.ChatException;
import com.example.ot.app.chat.repository.ChatMessageRepository;
import com.example.ot.app.chat.repository.ChatRoomAndChatMessageRepository;
import com.example.ot.app.chat.repository.ChatRoomRepository;
import com.example.ot.app.member.entity.Member;
import com.example.ot.app.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static com.example.ot.app.chat.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatMessageService {

    private final MemberService memberService;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomService chatRoomService;
    private final ChatRoomAndChatMessageRepository chatRoomAndChatMessageRepository;

    @Transactional
    public void sendMessage(MessageRequest messageRequest, Long memberId) {
        if(!Objects.equals(messageRequest.getWriterId(), memberId)){
            throw new ChatException(WRITER_AND_MEMBER_MISMATCH);
        }
        Member member = memberService.findByMemberId(memberId);
        ChatMessage chatMessage = ChatMessage.of(messageRequest.getMessage(), member);
        ChatRoom chatRoom = chatRoomService.findByChatRoomId(messageRequest.getRoomId());
        ChatRoomAndChatMessage chatRoomAndChatMessage = ChatRoomAndChatMessage.of(chatRoom, chatMessage);
        chatMessageRepository.save(chatMessage);
        chatRoomAndChatMessageRepository.save(chatRoomAndChatMessage);
    }

}
