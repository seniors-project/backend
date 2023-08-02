package com.seniors.domain.chat.repository;


import com.seniors.domain.chat.dto.ChatRoomMembersDto;
import com.seniors.domain.chat.entity.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChatRoomMembersRepositoryCustom {

	ChatRoom GetChatRoomRes(Long ChatRoomId);

	Page<ChatRoomMembersDto.GetChatRoomMembersRes> findAllChatRoomByUserId(Pageable pageable);
}
