package com.example.ot.app.chat.service;

import com.example.ot.app.board.entity.RecruitmentStatus;
import com.example.ot.app.board.entity.TravelBoard;
import com.example.ot.app.board.repository.TravelBoardRepository;
import com.example.ot.app.chat.dto.ChatRoomMessageDto;
import com.example.ot.app.chat.dto.response.ChatRoomIdResponse;
import com.example.ot.app.chat.dto.response.ShowChatRoomResponse;
import com.example.ot.app.chat.dto.response.ShowMyChatRoomsResponse;
import com.example.ot.app.chat.entity.ChatMessage;
import com.example.ot.app.chat.entity.ChatRoom;
import com.example.ot.app.chat.entity.ChatRoomAndMember;
import com.example.ot.app.chat.event.SendEnterMessageEvent;
import com.example.ot.app.chat.event.SendExitMessageEvent;
import com.example.ot.app.chat.exception.ChatException;
import com.example.ot.app.chat.repository.ChatMessageRepository;
import com.example.ot.app.chat.repository.ChatRoomAndChatMessageRepository;
import com.example.ot.app.chat.repository.ChatRoomAndMemberRepository;
import com.example.ot.app.chat.repository.ChatRoomRepository;
import com.example.ot.app.host.entity.Host;
import com.example.ot.app.host.exception.HostException;
import com.example.ot.app.host.repository.HostRepository;
import com.example.ot.app.member.entity.Member;
import com.example.ot.app.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.example.ot.app.chat.exception.ErrorCode.*;
import static com.example.ot.app.host.exception.ErrorCode.HOST_NOT_EXISTS;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomAndMemberRepository chatRoomAndMemberRepository;
    private final ChatRoomAndChatMessageRepository chatRoomAndChatMessageRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final TravelBoardRepository travelBoardRepository;
    private final MemberRepository memberRepository;
    private final HostRepository hostRepository;
    private final ApplicationEventPublisher publisher;

    @Transactional
    public void createChatRoomByTravelBoard(TravelBoard travelBoard, Member member) {
        ChatRoom chatRoomByTravelBoard = ChatRoom.ofBoard(travelBoard);
        ChatRoomAndMember chatRoomAndMember = ChatRoomAndMember.of(chatRoomByTravelBoard, member);
        chatRoomAndMemberRepository.save(chatRoomAndMember);
    }

    public ChatRoom findByChatRoomId(Long roomId){
        return chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ChatException(CHATROOM_NOT_EXISTS));
    }

    private int getChatMembersCount(Long roomId){
        return chatRoomAndMemberRepository.countByRoomId(roomId);
    }

    @Transactional
    public ShowChatRoomResponse getChatRoom(Long roomId, Long memberId) {
        if(!verifyChatRoomByMember(roomId, memberId)){
            verifyEnterChatRoom(roomId, memberId);
        }
        List<Long> chatMessageIdList = chatRoomAndChatMessageRepository.findAllChatMessageIdByChatRoomId(roomId);
        List<ChatRoomMessageDto> messageDtoList = convertToMessageDtoList(chatMessageIdList);
        return ShowChatRoomResponse.of(memberId, roomId, messageDtoList);
    }

    private void verifyEnterChatRoom(Long roomId, Long memberId){
        ChatRoom chatRoom = findByChatRoomId(roomId);
        if(!ObjectUtils.isEmpty(chatRoom.getHost())){
            throw new ChatException(CHATROOM_ACCESS_UNAUTHORIZED);
        }
        Long boardId = chatRoom.getBoardId();
        TravelBoard travelBoard = travelBoardRepository.findByBoardId(boardId);
        int currentNumber = getChatMembersCount(roomId);
        if(Objects.equals(travelBoard.getNumberOfTravelers(), currentNumber)){
            throw new ChatException(CHATROOM_FULL);
        }
        if(!Objects.equals(travelBoard.getRecruitmentStatus(), RecruitmentStatus.OPEN)){
            throw new ChatException(CHATROOM_ENTRY_PERIOD_EXPIRED);
        }
        enterChatRoom(chatRoom, memberId);
    }

    public void enterChatRoom(ChatRoom chatRoom, Long memberId){
        Member member = memberRepository.findByMemberId(memberId);
        ChatRoomAndMember chatRoomAndMember = ChatRoomAndMember.of(chatRoom, member);
        chatRoomAndMemberRepository.save(chatRoomAndMember);
        publisher.publishEvent(new SendEnterMessageEvent(chatRoom.getId(), member.getNickName()));
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

    @Transactional
    public ChatRoomIdResponse createHostChatRoom(Long hostMemberId, Long memberId) {
        if(Objects.equals(hostMemberId, memberId)){
            throw new ChatException(CHATROOM_HOST_CANNOT_CREATE_OWN);
        }
        Host host = hostRepository.findHostByMember_Id(hostMemberId)
                .orElseThrow(() -> new HostException(HOST_NOT_EXISTS));;
        Member hostMember = memberRepository.findByMemberId(hostMemberId);
        Member userMember = memberRepository.findByMemberId(memberId);
        ChatRoom chatRoom = ChatRoom.ofHost(host, hostMember, userMember);
        ChatRoomAndMember chatRoomAndMemberByHost = ChatRoomAndMember.of(chatRoom, hostMember);
        ChatRoomAndMember chatRoomAndMemberByUser = ChatRoomAndMember.of(chatRoom, userMember);
        chatRoomRepository.save(chatRoom);
        chatRoomAndMemberRepository.save(chatRoomAndMemberByHost);
        chatRoomAndMemberRepository.save(chatRoomAndMemberByUser);
        return ChatRoomIdResponse.ofChatRoomId(chatRoom);
    }

    @Transactional
    public void exitChatRoom(Long roomId, Long memberId) {
        ChatRoom chatRoom = findByChatRoomId(roomId);
        Member member = memberRepository.findByMemberId(memberId);
        int chatMembersCount = getChatMembersCount(roomId);
        if(!ObjectUtils.isEmpty(chatRoom.getTravelBoard())){
            canWriterLeaveChatRoom(chatRoom, member, chatMembersCount);
        }
        ChatRoomAndMember chatRoomAndMember = chatRoomAndMemberRepository.findByRoomIdAndMemberId(roomId, memberId);

        chatRoomAndMemberRepository.delete(chatRoomAndMember);

        if(chatMembersCount == 1){
            chatRoomAndChatMessageRepository.deleteAllByChatRoomId(roomId);
            chatRoomRepository.delete(chatRoom);
        }
        publisher.publishEvent(new SendExitMessageEvent(roomId, member.getNickName()));
    }

    private void canWriterLeaveChatRoom(ChatRoom chatRoom, Member member, int chatMembersCount){
        Member boardWriter = chatRoom.getBoardWriter();
        if(Objects.equals(member, boardWriter) && chatMembersCount > 1){
            throw new ChatException(CHATROOM_CANNOT_EXISTED);
        }
    }

    public List<ShowMyChatRoomsResponse> getMyChatRooms(Long memberId) {
        List<ChatRoom> myChatRoomList = chatRoomAndMemberRepository.findByMemberId(memberId);
        List<ShowMyChatRoomsResponse> showMyChatRoomsResponses = new ArrayList<>();
        for(ChatRoom chatRoom : myChatRoomList){
            ChatMessage chatMessage = chatRoomAndChatMessageRepository
                    .findLastByChatRoomId(chatRoom.getId()).orElse(null);
            ShowMyChatRoomsResponse showMyChatRoomsResponse = ShowMyChatRoomsResponse.of(chatRoom, chatMessage);
            showMyChatRoomsResponses.add(showMyChatRoomsResponse);
        }
        return showMyChatRoomsResponses;
    }

}
