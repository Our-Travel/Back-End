package com.example.ot.app.chat.repository;

import com.example.ot.app.chat.entity.ChatMessage;
import com.example.ot.app.chat.entity.ChatRoomAndChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRoomAndChatMessageRepository extends JpaRepository<ChatRoomAndChatMessage, Long> {

    @Query("select m.id from ChatRoomAndChatMessage c join c.chatMessage m join c.chatRoom r " +
            "where r.id = :roomId order by c.createdDate asc")
    List<Long> findAllChatMessageIdByChatRoomId(@Param("roomId") Long roomId);

    @Modifying
    @Query("delete from ChatRoomAndChatMessage c where c.chatRoom.id = :roomId")
    int deleteAllByChatRoomId(@Param("roomId") Long roomId);

    @Query("select m, w from ChatRoomAndChatMessage c join c.chatMessage m " +
            " join m.writer w where c.chatRoom.id = :roomId order by c.createdDate desc, c.id desc")
    Optional<ChatMessage> findLastByChatRoomId(@Param("roomId") Long roomId);
}
