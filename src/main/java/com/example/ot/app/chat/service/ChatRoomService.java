package com.example.ot.app.chat.service;

import com.example.ot.app.board.entity.TravelBoard;
import com.example.ot.app.chat.dto.ChatRoomMessageDto;
import com.example.ot.app.chat.dto.response.ShowChatRoomResponse;
import com.example.ot.app.chat.dto.response.ShowMyChatRoomsResponse;
import com.example.ot.app.chat.entity.ChatMessage;
import com.example.ot.app.chat.entity.ChatRoom;
import com.example.ot.app.chat.entity.ChatRoomAndChatMessage;
import com.example.ot.app.chat.entity.ChatRoomAndMember;
import com.example.ot.app.chat.exception.ChatException;
import com.example.ot.app.chat.repository.ChatMessageRepository;
import com.example.ot.app.chat.repository.ChatRoomAndChatMessageRepository;
import com.example.ot.app.chat.repository.ChatRoomAndMemberRepository;
import com.example.ot.app.chat.repository.ChatRoomRepository;
import com.example.ot.app.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.ot.app.chat.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomAndMemberRepository chatRoomAndMemberRepository;
    private final ChatRoomAndChatMessageRepository chatRoomAndChatMessageRepository;
    private final ChatMessageRepository chatMessageRepository;

    @Transactional
    public void createChatRoomByTravelBoard(TravelBoard travelBoard, Member member) {
        ChatRoom chatRoomByTravelBoard = ChatRoom.of(travelBoard);
        ChatRoomAndMember chatRoomAndMember = ChatRoomAndMember.of(chatRoomByTravelBoard, member);
        chatRoomRepository.save(chatRoomByTravelBoard);
        chatRoomAndMemberRepository.save(chatRoomAndMember);
    }

    public ChatRoom findByChatRoomId(Long chatRoomId){
        return chatRoomRepository.findById(chatRoomId).orElseThrow(() -> new ChatException(CHATROOM_NOT_EXISTS));
    }

    public ShowChatRoomResponse getChatRoom(Long roomId, Long memberId) {
        verifyChatRoomByMember(roomId, memberId);
        List<Long> chatMessageIdList = chatRoomAndChatMessageRepository.findAllChatMessageIdByChatRoomId(roomId);
        List<ChatRoomMessageDto> messageDtoList = convertToMessageDtoList(chatMessageIdList);
        return ShowChatRoomResponse.of(memberId, messageDtoList);
    }

    private List<ChatRoomMessageDto> convertToMessageDtoList(List<Long> chatMessageIdList) {
        List<ChatMessage> chatMessages = chatMessageRepository.findAllByIdIn(chatMessageIdList);
        return chatMessages.stream()
                .map(ChatRoomMessageDto::fromChatMessage)
                .collect(Collectors.toList());
    }

    private void verifyChatRoomByMember(Long roomId, Long memberId){
        boolean existsChatRoomByMember = chatRoomAndMemberRepository.existsByChatRoomIdAndMemberId(roomId, memberId);
        if(!existsChatRoomByMember){
            throw new ChatException(CHATROOM_ACCESS_UNAUTHORIZED);
        }
    }

//    public List<ShowMyChatRoomsResponse> getMyChatRooms(Long memberId) {
//
//    }
}
