package com.seniors.api.users.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seniors.api.users.domain.QUsers;
import com.seniors.api.users.domain.Users;
import com.seniors.common.repository.BasicRepoSupport;

import jakarta.persistence.EntityManager;

public class UsersRepositoryImpl extends BasicRepoSupport implements UsersRepositoryCustom {

	protected UsersRepositoryImpl(JPAQueryFactory jpaQueryFactory, EntityManager em) {
		super(jpaQueryFactory, em);
	}

	@Override
	public Users getOneUsers(Long userId) {
		Users user = jpaQueryFactory
				.selectFrom(QUsers.users)
				.where(QUsers.users.id.eq(userId))
				.fetchOne();
		return user;
	}
}
