package com.example.ot.app.chat.repository;

import com.example.ot.app.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("select r from ChatRoom r join fetch r.travelBoard b where r.id = :roomId")
    Optional<ChatRoom> findByChatRoomIdWithTravelBoard(@Param("roomId") Long roomId);

    @Query("select r.id from ChatRoom r join r.travelBoard b where b.id = :boardId")
    Optional<Long> findByBoardId(@Param("boardId") Long boardId);
}
