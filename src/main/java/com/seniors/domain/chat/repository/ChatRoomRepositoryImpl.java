package com.seniors.domain.chat.repository;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seniors.common.exception.type.NotFoundException;
import com.seniors.common.repository.BasicRepoSupport;
import com.seniors.domain.chat.dto.ChatRoomDto;
import com.seniors.domain.chat.dto.ChatRoomMembersDto;
import com.seniors.domain.chat.entity.*;
import com.seniors.domain.users.dto.UsersDto;
import com.seniors.domain.users.entity.QUsers;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class ChatRoomRepositoryImpl extends BasicRepoSupport implements ChatRoomRepositoryCustom {

	private final static QUsers users = QUsers.users;
	private final static QChatRoomMembers chatRoomMembers = QChatRoomMembers.chatRoomMembers;
	private final static QChatRoom chatRoom = QChatRoom.chatRoom;
	private final static QChatMessage chatMessage = QChatMessage.chatMessage;

	protected ChatRoomRepositoryImpl(JPAQueryFactory jpaQueryFactory, EntityManager em) {
		super(jpaQueryFactory, em);
	}

	@Override
	public ChatRoomDto.GetChatRoomRes findOneChatRoom(Long roomId) {
		List<ChatRoom> chatRoomResList = jpaQueryFactory
				.selectFrom(chatRoom)
				.leftJoin(chatRoom.chatMessages, chatMessage).fetchJoin()
				.where(chatRoom.id.eq(roomId))
				.fetch();

		if (chatRoomResList.isEmpty()) {
			throw new NotFoundException("ChatRoom Not Found");
		}

		List<ChatRoomDto.GetChatRoomRes> content = chatRoomResList.stream()
				.map(p -> new ChatRoomDto.GetChatRoomRes(
						p.getId(),
						p.getCreatedAt(),
						p.getLastModifiedDate(),
						p.getChatMessages())).toList();

		return content.get(0);
	}

	@Override
	public UsersDto.GetChatUserRes findAllChatRoom (Long userId) {

		List<ChatMessage> latestMessages = jpaQueryFactory
				.selectFrom(chatMessage)
				.where(chatMessage.id.in(
						JPAExpressions
								.select(chatMessage.id.max())
								.from(chatMessage)
								.where(chatMessage.chatRoom.id.in(
										JPAExpressions
												.select(chatRoomMembers.chatRoom.id)
												.from(chatRoomMembers)
												.where(chatRoomMembers.users.id.eq(userId))
												.groupBy(chatRoomMembers.chatRoom.id)
								))
								.groupBy(chatMessage.chatRoom.id)
				))
				.fetch();

		List<Long> chatRoomId = latestMessages.stream()
				.map(roomId -> roomId.getChatRoom().getId())
				.collect(Collectors.toList());

		List<ChatRoomMembers> chatRoomMembersList = jpaQueryFactory
				.selectFrom(chatRoomMembers)
				.where(
						chatRoomMembers.chatRoom.id.in(chatRoomId),
						chatRoomMembers.users.id.ne(userId)
				)
				.fetch();

		List<ChatRoomMembersDto.GetChatRoomMembersRes> getChatRoomMembersResList = new ArrayList<>();

		for (ChatRoomMembers roomMembers : chatRoomMembersList) {

			ChatRoomMembersDto.GetChatRoomMembersRes getChatRoomMembersRes =
					new ChatRoomMembersDto.GetChatRoomMembersRes(
							roomMembers.getChatRoom().getId(),
							roomMembers.getUsers().getId(),
							roomMembers.getRoomName(),
							roomMembers.getUsers().getProfileImageUrl(),
							latestMessages.get(getChatRoomMembersResList.size())
					);
			getChatRoomMembersResList.add(getChatRoomMembersRes);

		}

		UsersDto.GetChatUserRes getChatUserRes = new UsersDto.GetChatUserRes(userId, getChatRoomMembersResList);

		return getChatUserRes;

	}
}


