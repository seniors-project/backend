package com.seniors.domain.chat.repository;

import com.seniors.domain.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long>, ChatMessageRepositoryCustom {
//	Optional<ChatMessage> findByRoomMessage(String RoomMessage);
}
