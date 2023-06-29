package com.seniors.api.post.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seniors.api.post.domain.Post;
import com.seniors.api.post.domain.QPosts;
import com.seniors.common.exception.post.PostNotFound;
import com.seniors.common.repository.BasicRepoSupport;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class PostRepositoryImpl extends BasicRepoSupport implements PostRepositoryCustom {

	protected PostRepositoryImpl(JPAQueryFactory jpaQueryFactory, EntityManager em) {
		super(jpaQueryFactory, em);
	}

	@Override
	public Post getOnePost(Long postId) {
		Post post = jpaQueryFactory
				.selectFrom(QPosts.posts)
				.leftJoin(QPosts.posts.comments).fetchJoin()
				.where(QPosts.posts.id.eq(postId))
				.fetchOne();

		if (post == null)
			throw new PostNotFound();
		else {
			jpaQueryFactory.update(QPosts.posts)
					.set(QPosts.posts.viewCount, QPosts.posts.viewCount.add(1))
					.where(QPosts.posts.id.eq(postId))
					.execute();
			em.clear();
		}
		return post;
	}

}
