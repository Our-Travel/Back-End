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
import com.example.ot.app.member.entity.ProfileImage;
import com.example.ot.app.member.repository.MemberRepository;
import com.example.ot.app.member.repository.ProfileImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
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
    private final ProfileImageRepository profileImageRepository;
    private final ApplicationEventPublisher publisher;
    PageRequest pageRequest = PageRequest.of(0, 1);

    @Transactional
    public void createChatRoomByTravelBoard(TravelBoard travelBoard, Member member) {
        ChatRoom chatRoomByBoard = ChatRoom.createChatRoomByBoard(travelBoard);
        chatRoomRepository.save(chatRoomByBoard);
        ChatRoomAndMember chatRoomAndMember = ChatRoomAndMember.of(chatRoomByBoard, member);
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
        ChatRoom chatRoom = findByChatRoomId(roomId);
        Integer regionCode = null;
        String roomManager = null;
        String roomTitle = chatRoom.getTitle();
        if(!ObjectUtils.isEmpty(chatRoom.getHost())) {
            regionCode = chatRoom.getHost().getRegionCode();
        } else {
            regionCode = chatRoom.getTravelBoard().getRegionCode();
            roomManager = chatRoom.getBoardWriter().getNickName();
        }
        return new ShowChatRoomResponse(memberId, roomId, regionCode, roomManager, roomTitle, messageDtoList);
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

        ChatRoom chatRoom = ChatRoom.createChatRoomByHost(host, hostMember, userMember);
        List<Long> chatRoomListByHostUser = chatRoomAndMemberRepository.findChatRoomIdByHost(memberId);
        List<Long> chatRoomListByHostHost = chatRoomAndMemberRepository.findChatRoomIdByHost(hostMemberId);
        for (Long chatRoomId : chatRoomListByHostUser) {
            if (chatRoomListByHostHost.contains(chatRoomId)) {
                throw new ChatException(CHATROOM_EXISTS);
            }
        }
        ChatRoomAndMember chatRoomAndMemberByHost = ChatRoomAndMember.of(chatRoom, hostMember);
        ChatRoomAndMember chatRoomAndMemberByUser = ChatRoomAndMember.of(chatRoom, userMember);
        chatRoomRepository.save(chatRoom);
        chatRoomAndMemberRepository.save(chatRoomAndMemberByHost);
        chatRoomAndMemberRepository.save(chatRoomAndMemberByUser);
        return ChatRoomIdResponse.from(chatRoom.getId());
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
            return;
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
            List<ChatMessage> messages = chatRoomAndChatMessageRepository.findLastByChatRoomId(chatRoom.getId(), pageRequest);
            ProfileImage profileImage = null;
            if(!ObjectUtils.isEmpty(chatRoom.getHost())) {
                if(!Objects.equals(memberId, chatRoom.getHost().getMemberId())){
                    Long hostMemberId = chatRoom.getHost().getMemberId();
                    profileImage = profileImageRepository.findProfileImageByMemberId(hostMemberId).orElse(null);
                } else{
                    Long otherMemberId = chatRoomAndMemberRepository.findByOtherMemberId(memberId, chatRoom.getId());
                    profileImage = profileImageRepository.findProfileImageByMemberId(otherMemberId).orElse(null);
                }
            }
            ShowMyChatRoomsResponse showMyChatRoomsResponse = ShowMyChatRoomsResponse.of(chatRoom, messages, profileImage);
            showMyChatRoomsResponses.add(showMyChatRoomsResponse);
        }
        return showMyChatRoomsResponses;
    }

}
