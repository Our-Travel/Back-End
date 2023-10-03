package com.example.ot.app.chat.repository;

import com.example.ot.app.chat.entity.ChatRoom;
import com.example.ot.app.chat.exception.ChatException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

import static com.example.ot.app.chat.exception.ErrorCode.CHATROOM_NOT_EXISTS;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

//    Optional<ChatRoom> findByChatRoomId(Long roomId);
    default ChatRoom findByChatRoomId(Long roomId){
        return findById(roomId).orElseThrow(() -> new ChatException(CHATROOM_NOT_EXISTS));
    }

    @Query("select r.id from ChatRoom r join r.travelBoard b where b.id = :boardId")
    Optional<Long> findChatRoomByBoardId(@Param("boardId") Long boardId);

    @Query("select r.id from ChatRoom r where r.host.id = :hostId and r.travelBoard = null")
    List<Long> findByHostId(@Param("hostId") Long hostId);
}
