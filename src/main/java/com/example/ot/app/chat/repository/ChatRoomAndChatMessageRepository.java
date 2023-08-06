package com.example.ot.app.chat.repository;

import com.example.ot.app.chat.entity.ChatRoomAndChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomAndChatMessageRepository extends JpaRepository<ChatRoomAndChatMessage, Long> {
}
