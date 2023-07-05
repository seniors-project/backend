package com.seniors.domain.post.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seniors.domain.post.entity.Post;
import com.seniors.domain.post.entity.QPost;
import com.seniors.common.exception.type.NotFoundException;
import com.seniors.common.repository.BasicRepoSupport;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class PostRepositoryImpl extends BasicRepoSupport implements PostRepositoryCustom {

	protected PostRepositoryImpl(JPAQueryFactory jpaQueryFactory, EntityManager em) {
		super(jpaQueryFactory, em);
	}

	@Override
	public Post getOnePost(Long postId) {
		Post post = jpaQueryFactory
				.selectFrom(QPost.post)
				.leftJoin(QPost.post.comments).fetchJoin()
				.where(QPost.post.id.eq(postId))
				.fetchOne();
		if (post == null) {
			throw new NotFoundException("Post Not Found");
		}
		updateViewCount(post.getId());
		em.clear();
		return post;
	}

	private void updateViewCount(Long postId) {
		jpaQueryFactory
				.update(QPost.post)
				.set(QPost.post.viewCount, QPost.post.viewCount.add(1))
				.where(QPost.post.id.eq(postId))
				.execute();
	}
}

