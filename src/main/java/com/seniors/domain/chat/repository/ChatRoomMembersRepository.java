package com.seniors.domain.chat.repository;

import com.seniors.domain.chat.entity.ChatRoomMembers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomMembersRepository extends JpaRepository<ChatRoomMembers, Long> {
    Optional<ChatRoomMembers> findByChatRoomIdAndUsersId(Long chatRoomId, Long userId);
}
