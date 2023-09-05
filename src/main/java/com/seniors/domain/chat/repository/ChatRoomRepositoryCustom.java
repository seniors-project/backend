package com.seniors.domain.chat.repository;


import com.seniors.domain.chat.dto.ChatRoomDto;
import com.seniors.domain.users.dto.UsersDto;

public interface ChatRoomRepositoryCustom {

	ChatRoomDto.GetChatRoomRes findOneChatRoom(Long roomId);

	UsersDto.GetChatUserRes findAllChatRoom(Long userId);


}
