package com.seniors.domain.notification.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seniors.common.repository.BasicRepoSupport;
import com.seniors.domain.notification.dto.NotificationDto;
import com.seniors.domain.notification.entity.Notification;
import com.seniors.domain.notification.entity.QNotification;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class NotificationRepositoryImpl extends BasicRepoSupport implements NotificationRepositoryCustom {

	private static final QNotification notification = QNotification.notification;
	protected NotificationRepositoryImpl(JPAQueryFactory jpaQueryFactory, EntityManager em) {
		super(jpaQueryFactory, em);
	}

	@Override
	public Page<NotificationDto> findAllByUsersId(Long userId, Pageable pageable) {
		JPAQuery<Notification> query = jpaQueryFactory
				.selectFrom(notification)
				.where(notification.isRead.eq(false))
				.fetchJoin();
		super.setPageQuery(query, pageable, notification);
		List<NotificationDto> content = query.fetch().stream()
				.map(n -> new NotificationDto(
						n.getId(),
						n.getUsers().getId(),
						n.getContent(),
						n.getUrl(),
						n.getCreatedAt(),
						n.isRead()
				)).toList();

		JPAQuery<Long> countQuery = jpaQueryFactory
				.select(notification.id.count())
				.from(notification);
		Long count = countQuery.fetchOne();
		count = count == null ? 0 : count;

		return new PageImpl<>(content, super.getValidPageable(pageable), count);
	}
}
