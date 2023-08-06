package com.example.ot.app.chat.service;

import com.example.ot.app.board.entity.TravelBoard;
import com.example.ot.app.chat.entity.ChatRoom;
import com.example.ot.app.chat.entity.ChatRoomAndMember;
import com.example.ot.app.chat.repository.ChatMessageRepository;
import com.example.ot.app.chat.repository.ChatRoomAndMemberRepository;
import com.example.ot.app.chat.repository.ChatRoomRepository;
import com.example.ot.app.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomAndMemberRepository chatRoomAndMemberRepository;

    @Transactional
    public void createChatRoomByTravelBoard(TravelBoard travelBoard, Member member) {
        ChatRoom chatRoomByTravelBoard = ChatRoom.of(travelBoard);
        ChatRoomAndMember chatRoomAndMember = ChatRoomAndMember.of(chatRoomByTravelBoard, member);
        chatRoomRepository.save(chatRoomByTravelBoard);
        chatRoomAndMemberRepository.save(chatRoomAndMember);
    }
}
