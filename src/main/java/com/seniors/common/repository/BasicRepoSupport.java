package com.seniors.common.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BeanPath;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import jakarta.persistence.EntityManager;

@SuppressWarnings("unchecked")
public abstract class BasicRepoSupport extends QuerydslRepositorySupport {

	protected final JPAQueryFactory jpaQueryFactory;
	protected final EntityManager em;

	protected BasicRepoSupport(JPAQueryFactory jpaQueryFactory, EntityManager em) {
		super(jpaQueryFactory.getClass());
		this.jpaQueryFactory = jpaQueryFactory;
		this.em = em;
	}

	protected <T> void setPageQuery(JPAQuery<?> query, Pageable pageable, BeanPath<T> clazz) {
		if (pageable == null) {
			return;
		}
		query.offset(pageable.getOffset())
				.limit(pageable.getPageSize());

		this.setOrderQuery(query, pageable, clazz);
	}

	protected <T> void setOrderQuery(JPAQuery<?> query, Pageable pageable, BeanPath<T> clazz) {
		if (pageable == null) {
			return;
		}

		for (Sort.Order o : pageable.getSort()) {
			PathBuilder<Object> pathBuilder = new PathBuilder<>(
					clazz.getType(), clazz.getMetadata());
			query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
					pathBuilder.get(o.getProperty())));
		}
	}

	protected Pageable getValidPageable(Pageable pageable) {
		// excel 요청시 pageable null 로 넘어옴.
		return (pageable == null) ? PageRequest.of(0, 10) : pageable;
	}

}