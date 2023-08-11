package com.example.ot.app.chat.service;

import com.example.ot.app.board.entity.TravelBoard;
import com.example.ot.app.board.service.TravelBoardService;
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
import com.example.ot.app.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    private final TravelBoardService travelBoardService;
    private final MemberService memberService;

    @Transactional
    public void createChatRoomByTravelBoard(TravelBoard travelBoard, Member member) {
        ChatRoom chatRoomByTravelBoard = ChatRoom.of(travelBoard);
        ChatRoomAndMember chatRoomAndMember = ChatRoomAndMember.of(chatRoomByTravelBoard, member);
        chatRoomRepository.save(chatRoomByTravelBoard);
        chatRoomAndMemberRepository.save(chatRoomAndMember);
    }

    public ChatRoom findByChatRoomId(Long roomId){
        return chatRoomRepository.findById(roomId).orElseThrow(() -> new ChatException(CHATROOM_NOT_EXISTS));
    }

    public ChatRoom findByChatRoomIdWithTravelBoard(Long roomId){
        return chatRoomRepository.findByChatRoomIdWithTravelBoard(roomId).orElseThrow(() -> new ChatException(CHATROOM_NOT_EXISTS));
    }

    public ShowChatRoomResponse getChatRoom(Long roomId, Long memberId) {
        if(!verifyChatRoomByMember(roomId, memberId)){
            validEnterChatRoom(roomId, memberId);
        }
        List<Long> chatMessageIdList = chatRoomAndChatMessageRepository.findAllChatMessageIdByChatRoomId(roomId);
        List<ChatRoomMessageDto> messageDtoList = convertToMessageDtoList(chatMessageIdList);
        return ShowChatRoomResponse.of(memberId, messageDtoList);
    }

    private void validEnterChatRoom(Long roomId, Long memberId){
        ChatRoom chatRoom = findByChatRoomIdWithTravelBoard(roomId);
        Long boardId = chatRoom.getTravelBoard().getId();
        TravelBoard travelBoard = travelBoardService.findByBoardId(boardId);
        int currentNumber = chatRoomAndMemberRepository.countByRoomId(roomId);
        if(Objects.equals(travelBoard.getNumberOfTravelers(), currentNumber)){
            throw new ChatException(CHATROOM_FULL);
        }
        enterChatRoom(chatRoom, memberId);
    }

    @Transactional
    public void enterChatRoom(ChatRoom chatRoom, Long memberId){
        Member member = memberService.findByMemberId(memberId);
        ChatRoomAndMember chatRoomAndMember = ChatRoomAndMember.of(chatRoom, member);
        chatRoomAndMemberRepository.save(chatRoomAndMember);
    }

    private List<ChatRoomMessageDto> convertToMessageDtoList(List<Long> chatMessageIdList) {
        List<ChatMessage> chatMessages = chatMessageRepository.findAllByIdIn(chatMessageIdList);
        return chatMessages.stream()
                .map(ChatRoomMessageDto::fromChatMessage)
                .collect(Collectors.toList());
    }

    private boolean verifyChatRoomByMember(Long roomId, Long memberId){
        return chatRoomAndMemberRepository.existsByChatRoomIdAndMemberId(roomId, memberId);
    }

//    public List<ShowMyChatRoomsResponse> getMyChatRooms(Long memberId) {
//
//    }
}
