package com.seniors.domain.post.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seniors.common.exception.type.NotFoundException;
import com.seniors.common.repository.BasicRepoSupport;
import com.seniors.domain.comment.entity.QComment;
import com.seniors.domain.post.dto.PostDto.GetPostRes;
import com.seniors.domain.post.entity.Post;
import com.seniors.domain.post.entity.QPost;
import com.seniors.domain.users.entity.QUsers;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;


@Slf4j
@Repository
public class PostRepositoryImpl extends BasicRepoSupport implements PostRepositoryCustom {

	private final static QPost post = QPost.post;
	private final static QComment comment = QComment.comment;
	private final static QUsers users = QUsers.users;

	protected PostRepositoryImpl(JPAQueryFactory jpaQueryFactory, EntityManager em) {
		super(jpaQueryFactory, em);
	}

	@Override
	public GetPostRes getOnePost(Long postId) {
		List<Post> postResList = jpaQueryFactory
				.selectFrom(post)
				.leftJoin(post.comments, comment).fetchJoin()
				.innerJoin(post.users, users).fetchJoin()
				.where(post.id.eq(postId))
				.fetch();

		if (postResList.isEmpty()) {
			throw new NotFoundException("Post Not Found");
		}

		List<GetPostRes> content = postResList.stream()
				.map(p -> new GetPostRes(
						p.getId(),
						p.getTitle(),
						p.getContent(),
						p.getViewCount(),
						p.getCreatedAt(),
						p.getLastModifiedDate(),
						p.getUsers(),
						p.getComments())).toList();

		updateViewCount(content.get(0).getPostId());
		return content.get(0);
	}


	private void updateViewCount(Long postId) {
		jpaQueryFactory
				.update(QPost.post)
				.set(QPost.post.viewCount, QPost.post.viewCount.add(1))
				.where(QPost.post.id.eq(postId))
				.execute();
	}
}

