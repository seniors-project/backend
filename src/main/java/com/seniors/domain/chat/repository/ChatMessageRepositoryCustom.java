package com.seniors.domain.chat.repository;


import com.seniors.domain.chat.entity.ChatMessage;

public interface ChatMessageRepositoryCustom {

	ChatMessage GetChatMessageRes(Long ChatMessageId);
}
