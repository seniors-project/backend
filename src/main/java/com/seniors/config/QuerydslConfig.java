package com.seniors.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Configuration
public class QuerydslConfig {

	@PersistenceContext
	public EntityManager em;

	@Bean
	public JPAQueryFactory jpaQueryFactory() {
		return new JPAQueryFactory(em);
	}
}
