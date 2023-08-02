package com.seniors.domain.chat.repository;


import com.seniors.domain.chat.dto.ChatRoomDto;

public interface ChatRoomRepositoryCustom {

	ChatRoomDto.GetChatRoomRes findOneChatRoom(Long roomId);

//	Page<ChatRoomDto.GetChatRoomRes> findAllChatRoom(Pageable pageable);
}
