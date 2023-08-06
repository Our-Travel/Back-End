package com.example.ot.app.chat.repository;

import com.example.ot.app.chat.entity.ChatRoomAndMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomAndMemberRepository extends JpaRepository<ChatRoomAndMember, Long> {
}
