package com.seniors.domain.chat.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seniors.common.repository.BasicRepoSupport;
import com.seniors.domain.chat.entity.QChatMessage;
import com.seniors.domain.chat.entity.QChatRoom;
import com.seniors.domain.chat.entity.QChatRoomMembers;
import com.seniors.domain.users.entity.QUsers;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChatRoomMembersRepositoryImpl extends BasicRepoSupport implements ChatRoomMembersRepositoryCustom {

	private final static QChatRoom chatRoom = QChatRoom.chatRoom;
	private final static QUsers users = QUsers.users;
	private final static QChatMessage chatMessage = QChatMessage.chatMessage;
	private final static QChatRoomMembers chatRoomUsers = QChatRoomMembers.chatRoomMembers;

	protected ChatRoomMembersRepositoryImpl(JPAQueryFactory jpaQueryFactory, EntityManager em) {
		super(jpaQueryFactory, em);
	}
}
