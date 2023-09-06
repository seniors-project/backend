package com.seniors.domain.chat.repository;


import java.util.Optional;

public interface ChatRoomMembersRepositoryCustom {

    Optional<Long> findChatRoomIdByUserIds(Long userId1, Long userId2);

}
