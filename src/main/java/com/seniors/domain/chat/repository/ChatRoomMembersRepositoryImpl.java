package com.seniors.domain.chat.repository;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seniors.common.repository.BasicRepoSupport;
import com.seniors.domain.chat.entity.QChatMessage;
import com.seniors.domain.chat.entity.QChatRoom;
import com.seniors.domain.chat.entity.QChatRoomMembers;
import com.seniors.domain.users.entity.QUsers;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
public class ChatRoomMembersRepositoryImpl extends BasicRepoSupport implements ChatRoomMembersRepositoryCustom {

	private final static QChatRoom chatRoom = QChatRoom.chatRoom;
	private final static QUsers users = QUsers.users;
	private final static QChatMessage chatMessage = QChatMessage.chatMessage;
	private final static QChatRoomMembers chatRoomMembers = QChatRoomMembers.chatRoomMembers;

	protected ChatRoomMembersRepositoryImpl(JPAQueryFactory jpaQueryFactory, EntityManager em) {
		super(jpaQueryFactory, em);
	}

	public Optional<Long> findChatRoomIdByUserIds(Long userId1, Long userId2) {

		QChatRoomMembers subChatRoomMembers = new QChatRoomMembers("subChatRoomMembers");
		Long chatRoomIds = jpaQueryFactory
				.selectDistinct(chatRoomMembers.chatRoom.id)
				.from(chatRoomMembers)
				.where(chatRoomMembers.chatRoom.id.in(
						jpaQueryFactory
								.select(subChatRoomMembers.chatRoom.id)
								.from(subChatRoomMembers)
								.where(subChatRoomMembers.users.id.eq(userId1))
				))
				.where(chatRoomMembers.chatRoom.id.in(
						jpaQueryFactory
								.select(subChatRoomMembers.chatRoom.id)
								.from(subChatRoomMembers)
								.where(subChatRoomMembers.users.id.eq(userId2))
				))
				.fetchFirst();

		return Optional.of(chatRoomIds != null ? chatRoomIds : 0L);
	}
}
