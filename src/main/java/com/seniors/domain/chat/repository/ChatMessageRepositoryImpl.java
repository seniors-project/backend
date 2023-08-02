package com.seniors.domain.chat.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seniors.common.repository.BasicRepoSupport;
import com.seniors.domain.chat.entity.ChatMessage;
import jakarta.persistence.EntityManager;

public class ChatMessageRepositoryImpl extends BasicRepoSupport implements ChatMessageRepositoryCustom {

	protected ChatMessageRepositoryImpl(JPAQueryFactory jpaQueryFactory, EntityManager em) {
		super(jpaQueryFactory, em);
	}

	@Override
	public ChatMessage GetChatMessageRes(Long ChatMessageId) {
		return null;
	}
}
