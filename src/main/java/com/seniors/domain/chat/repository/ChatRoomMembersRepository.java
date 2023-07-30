package com.seniors.domain.chat.repository;

import com.seniors.domain.chat.entity.ChatRoomMembers;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomMembersRepository extends JpaRepository<ChatRoomMembers, Long> {
}
