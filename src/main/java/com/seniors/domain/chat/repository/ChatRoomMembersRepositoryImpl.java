package com.seniors.domain.chat.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seniors.common.repository.BasicRepoSupport;
import com.seniors.domain.chat.entity.QChatRoomMembers;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class ChatRoomMembersRepositoryImpl extends BasicRepoSupport implements ChatRoomMembersRepositoryCustom {
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

		Optional<Long> chatRoomId = Optional.empty();

		if (chatRoomIds != null) {
			chatRoomId = chatRoomIds.describeConstable();
		}

		return chatRoomId;
	}
}
