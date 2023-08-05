package com.seniors.domain.notification.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seniors.common.repository.BasicRepoSupport;
import com.seniors.domain.notification.dto.NotificationDto;
import com.seniors.domain.notification.entity.Notification;
import com.seniors.domain.notification.entity.QNotification;
import com.seniors.domain.users.entity.QUsers;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class NotificationRepositoryImpl extends BasicRepoSupport implements NotificationRepositoryCustom {

	private final static QNotification notification = QNotification.notification;
	private final static QUsers users = QUsers.users;
	protected NotificationRepositoryImpl(JPAQueryFactory jpaQueryFactory, EntityManager em) {
		super(jpaQueryFactory, em);
	}

	@Override
	public Slice<NotificationDto> findNotificationList(Long userId, Pageable pageable, Long lastId) {
		List<Notification> results = jpaQueryFactory
				.selectFrom(notification)
				.join(notification.users, users).fetchJoin()
				.where(
						userIdEq(userId),
						ltNotificationId(lastId),
						notification.isRead.eq(false))
				.orderBy(notification.id.desc())
				.limit(pageable.getPageSize() + 1)
				.fetch();
		List<NotificationDto> content = results.stream()
				.map(n -> new NotificationDto(
						n.getId(),
						n.getUsers().getId(),
						n.getContent(),
						n.getUrl(),
						n.getCreatedAt(),
						n.isRead()
				))
				.collect(Collectors.toList());

		return checkLastPage(pageable, content);
	}

	private BooleanExpression userIdEq(Long userId) {
		return notification.users.id.ne(userId);
	}

	// no-offset 방식 처리하는 메서드
	private BooleanExpression ltNotificationId(Long lastId) {
		return lastId == null ? null : notification.id.lt(lastId);
	}

	// 무한 스크롤 방식 처리하는 메서드
	private Slice<NotificationDto> checkLastPage(Pageable pageable, List<NotificationDto> resultDtos) {

		boolean hasNext = false;

		// 조회한 결과 개수가 요청한 페이지 사이즈보다 크면 뒤에 더 있음, next = true
		if (resultDtos.size() > pageable.getPageSize()) {
			hasNext = true;
			resultDtos.remove(pageable.getPageSize());
		}
		return new SliceImpl<>(resultDtos, pageable, hasNext);
	}
}
