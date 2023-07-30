package com.seniors.domain.post.repository.postMedia;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seniors.common.repository.BasicRepoSupport;
import com.seniors.domain.post.entity.PostMedia;
import com.seniors.domain.post.entity.QPostMedia;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class PostMediaRepositoryImpl extends BasicRepoSupport implements PostMediaRepositoryCustom {

	private final static QPostMedia postMedia = QPostMedia.postMedia;

	protected PostMediaRepositoryImpl(JPAQueryFactory jpaQueryFactory, EntityManager em) {
		super(jpaQueryFactory, em);
	}

	@Override
	public void deleteByPostId(Long postId) {
		jpaQueryFactory.update(postMedia)
				.set(postMedia.isDeleted, true)
				.where(postMedia.post.id.eq(postId))
				.execute();
	}

	@Override
	public List<PostMedia> findByPostId(Long postId) {
		return jpaQueryFactory.selectFrom(postMedia)
				.where(postMedia.post.id.eq(postId))
				.fetch();
	}
}
