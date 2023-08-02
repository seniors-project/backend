package com.seniors.domain.chat.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seniors.common.repository.BasicRepoSupport;
import com.seniors.domain.chat.dto.ChatRoomMembersDto;
import com.seniors.domain.chat.entity.*;
import com.seniors.domain.users.entity.QUsers;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Slf4j
public class ChatRoomMembersRepositoryImpl extends BasicRepoSupport implements ChatRoomMembersRepositoryCustom {

	private final static QChatRoom chatRoom = QChatRoom.chatRoom;
	private final static QUsers users = QUsers.users;
	private final static QChatMessage chatMessage = QChatMessage.chatMessage;
	private final static QChatRoomMembers chatRoomUsers = QChatRoomMembers.chatRoomMembers;

	protected ChatRoomMembersRepositoryImpl(JPAQueryFactory jpaQueryFactory, EntityManager em) {
		super(jpaQueryFactory, em);
	}

	@Override
	public ChatRoom GetChatRoomRes(Long ChatRoomId) {
		return null;
	}

	@Override
	public Page<ChatRoomMembersDto.GetChatRoomMembersRes> findAllChatRoomByUserId(Pageable pageable) {
		JPAQuery<ChatRoomMembers> query = jpaQueryFactory
				.selectFrom(chatRoomUsers)
				.leftJoin(chatRoomUsers.users, users).fetchJoin();
		super.setPageQuery(query, pageable, chatRoomUsers);
		List<ChatRoomMembersDto.GetChatRoomMembersRes> content = query.fetch().stream()
				.map(p -> new ChatRoomMembersDto.GetChatRoomMembersRes(
						p.getRoomName())).toList();
		JPAQuery<Long> countQuery = jpaQueryFactory
				.select(chatRoom.id.count())
				.from(chatRoom);
		Long count = countQuery.fetchOne();
		count = count == null ? 0 : count;

		return new PageImpl<>(content, super.getValidPageable(pageable), count);
	}
}
