package com.side.api.post.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.side.api.post.domain.Post;
import com.side.api.post.domain.QPost;
import com.side.common.exception.post.PostNotFound;
import com.side.common.repository.BasicRepoSupport;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class PostRepositoryImpl extends BasicRepoSupport implements PostRepositoryCustom {


	protected PostRepositoryImpl(JPAQueryFactory jpaQueryFactory, EntityManager em) {
		super(jpaQueryFactory, em);
	}

	@Override
	public Post getPostOne(Long postId) {
		Post post = jpaQueryFactory
				.selectFrom(QPost.post)
				.leftJoin(QPost.post.comments).fetchJoin()
				.where(QPost.post.id.eq(postId))
				.where(QPost.post.isDeleted.eq(0))
				.where(QPost.post.display.eq(1))
				.fetchOne();

		if (post == null)
			throw new PostNotFound();
		else {
			jpaQueryFactory.update(QPost.post)
					.set(QPost.post.viewCount, QPost.post.viewCount.add(1))
					.where(QPost.post.id.eq(postId))
					.execute();
			em.clear();
		}
		return post;
	}

}
