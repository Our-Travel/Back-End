package com.example.ot.app.chat.repository;

import com.example.ot.app.chat.entity.ChatRoom;
import com.example.ot.app.chat.entity.ChatRoomAndMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRoomAndMemberRepository extends JpaRepository<ChatRoomAndMember, Long> {

    boolean existsByChatRoomIdAndMemberId(Long roomId, Long memberId);

    @Query("select count(c.id) from ChatRoomAndMember c join c.chatRoom r where r.id = :roomId")
    int countByRoomId(@Param("roomId") Long roomId);

    @Query("select c.chatRoom from ChatRoomAndMember c join c.member m where m.id = :memberId")
    List<ChatRoom> findByMemberId(@Param("memberId")Long memberId);

    @Query("select c from ChatRoomAndMember c where c.chatRoom.id = :roomId and c.member.id = :memberId")
    ChatRoomAndMember findByRoomIdAndMemberId(@Param("roomId")Long roomId, @Param("memberId")Long memberId);

    @Query("select c.chatRoom.id from ChatRoomAndMember c where c.member.id = :memberId and c.chatRoom.travelBoard = null")
    List<Long> findChatRoomIdByHost(@Param("memberId")Long memberId);

    @Query("select c.member.id from ChatRoomAndMember c where c.chatRoom.id = :roomId and c.member.id != :memberId")
    Long findByOtherMemberId(@Param("memberId")Long memberId, @Param("roomId")Long roomId);
}
