package com.seniors.domain.chat.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seniors.common.exception.type.NotFoundException;
import com.seniors.common.repository.BasicRepoSupport;
import com.seniors.domain.chat.dto.ChatRoomDto;
import com.seniors.domain.chat.entity.ChatRoom;
import com.seniors.domain.chat.entity.QChatMessage;
import com.seniors.domain.chat.entity.QChatRoom;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class ChatRoomRepositoryImpl extends BasicRepoSupport implements ChatRoomRepositoryCustom {

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


}
