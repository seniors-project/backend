package com.seniors.domain.users.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seniors.common.repository.BasicRepoSupport;
import com.seniors.domain.chat.dto.ChatRoomMembersDto.GetChatRoomMembersRes;
import com.seniors.domain.chat.entity.QChatRoom;
import com.seniors.domain.chat.entity.QChatRoomMembers;
import com.seniors.domain.post.dto.PostDto;
import com.seniors.domain.post.entity.Post;
import com.seniors.domain.users.dto.UsersDto.GetChatUserRes;
import com.seniors.domain.users.entity.QUsers;
import com.seniors.domain.users.entity.Users;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;


import java.util.List;

public class UsersRepositoryImpl extends BasicRepoSupport implements UsersRepositoryCustom {

	protected UsersRepositoryImpl(JPAQueryFactory jpaQueryFactory, EntityManager em) {
		super(jpaQueryFactory, em);
	}

	private final static QUsers users = QUsers.users;
	private final static QChatRoomMembers chatRoomMembers = QChatRoomMembers.chatRoomMembers;
	private final static QChatRoom chatRoom = QChatRoom.chatRoom;

	@Override
	public Users getOneUsers(Long userId) {
		Users user = jpaQueryFactory
				.selectFrom(QUsers.users)
				.where(QUsers.users.id.eq(userId))
				.fetchOne();
		return user;
	}

//	@Override
//	public Page<GetChatUserRes> findAllChatRoom(Long userId, Pageable pageable) {
//		JPAQuery<Users> query = jpaQueryFactory
//				.selectFrom(users)
//				.where(users.id.eq(userId))
//				.leftJoin(users.chatRoomMembers, chatRoomMembers).fetchJoin();
//		super.setPageQuery(query, pageable, users);
//		List<GetChatUserRes> content = query.fetch().stream()
//				.map(p -> new GetChatUserRes(
//						p.getId(),
//						p.getNickname(),
//						p.getProfileImageUrl(),
//						p.getChatRoomMembers())).toList();
//
//		JPAQuery<Long> countQuery = jpaQueryFactory
//				.select(users.id.count())
//				.from(users);
//		Long count = countQuery.fetchOne();
//		count = count == null ? 0 : count;
//
//		return new PageImpl<>(content, super.getValidPageable(pageable), count);
//	}
	@Override
	public Page<GetChatUserRes> findAllChatRoom(Long userId, Pageable pageable) {
		JPAQuery<Users> query = jpaQueryFactory
				.selectFrom(users)
				.leftJoin(users.chatRoomMembers, chatRoomMembers).fetchJoin()
				.where(users.id.eq(userId));
//				.join(chatRoomMembers.chatRoom, chatRoom);
//				.leftJoin(chatRoomMembers.chatRoom, chatRoom).fetchJoin()
//				.distinct();
		super.setPageQuery(query, pageable, users);
		List<GetChatUserRes> content = query.fetch().stream()
				.map(p -> new GetChatUserRes(
						p.getId(),
						p.getNickname(),
						p.getProfileImageUrl(),
						p.getChatRoomMembers())).toList();

		JPAQuery<Long> countQuery = jpaQueryFactory
				.select(users.id.count())
				.from(users);
		Long count = countQuery.fetchOne();
		count = count == null ? 0 : count;

		return new PageImpl<>(content, super.getValidPageable(pageable), count);
	}
}
