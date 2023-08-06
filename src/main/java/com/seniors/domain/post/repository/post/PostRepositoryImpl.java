package com.seniors.domain.post.repository.post;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seniors.common.exception.type.NotFoundException;
import com.seniors.common.repository.BasicRepoSupport;
import com.seniors.domain.comment.entity.QComment;
import com.seniors.domain.post.dto.PostDto.GetPostRes;
import com.seniors.domain.post.dto.PostDto.ModifyPostReq;
import com.seniors.domain.post.entity.*;
import com.seniors.domain.users.entity.QUsers;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Repository
public class PostRepositoryImpl extends BasicRepoSupport implements PostRepositoryCustom {

	private final static QPost post = QPost.post;
	private final static QPostMedia postMedia = QPostMedia.postMedia;
	private final static QComment comment = QComment.comment;
	private final static QUsers users = QUsers.users;
	private final static QPostLike postLike = QPostLike.postLike;
	private final static QPostLikeEmbedded postLikeEmbedded = QPostLikeEmbedded.postLikeEmbedded;

	protected PostRepositoryImpl(JPAQueryFactory jpaQueryFactory, EntityManager em) {
		super(jpaQueryFactory, em);
	}

	@Override
	public GetPostRes findOnePost(Long postId, Long userId) {

		Post posts = jpaQueryFactory
				.selectFrom(post)
				.innerJoin(post.users, users).fetchJoin()
				.leftJoin(post.comments, comment).fetchJoin()
				.leftJoin(post.postMedias, postMedia)
				.leftJoin(post.postLikes, postLike)
				.where(post.id.eq(postId))
				.fetchOne();

		if (posts == null) {
			throw new NotFoundException("Post Not Found");
		}

		Boolean filteredPostLikes = posts.getPostLikes().stream()
				.filter(postLike -> postLike.getUsers().getId().equals(userId))
				.map(PostLike::getStatus)
				.findFirst() // 첫 번째 요소 가져오거나 없으면 Optional.empty 반환
				.orElse(false); // 리스트가 비어있을 경우 기본값으로 false 설정

		return new GetPostRes(
				posts.getId(),
				posts.getTitle(),
				posts.getContent(),
				filteredPostLikes,
				posts.getCreatedAt(),
				posts.getLastModifiedDate(),
				posts.getUsers(),
				posts.getComments(),
				posts.getPostMedias()
		);
	}

	public void modifyPost(String title, String content, Long postId, Long userId) {
		jpaQueryFactory
				.update(post)
				.set(post.title, title)
				.set(post.content, content)
				.where(post.id.eq(postId).and(post.users.id.eq(userId)))
				.execute();
	}

	@Override
	public Page<GetPostRes> findAllPost(Pageable pageable, Long userId) {
		JPAQuery<Post> query = jpaQueryFactory
				.selectFrom(post)
				.innerJoin(post.users, users).fetchJoin()
				.leftJoin(post.comments, comment).fetchJoin()
				.leftJoin(post.postMedias, postMedia)
				.leftJoin(post.postLikes, postLike); // LAZY로 변경한 후에는 fetchJoin으로 가져올 필요 없음

		super.setPageQuery(query, pageable, post);
		List<Post> results = query.fetch();

		List<GetPostRes> content = results.stream()
				.map(p -> {
					Boolean filteredPostLikes = p.getPostLikes().stream()
							.filter(postLike -> postLike.getUsers().getId().equals(userId))
							.map(PostLike::getStatus)
							.findFirst() // 첫 번째 요소 가져오거나 없으면 Optional.empty 반환
							.orElse(false); // 리스트가 비어있을 경우 기본값으로 false 설정

					return new GetPostRes(
							p.getId(),
							p.getTitle(),
							p.getContent(),
							filteredPostLikes,
							p.getCreatedAt(),
							p.getLastModifiedDate(),
							p.getUsers(),
							p.getComments(),
							p.getPostMedias()
					);
				}).toList();

		JPAQuery<Long> countQuery = jpaQueryFactory
				.select(post.id.count())
				.from(post);
		Long count = countQuery.fetchOne();
		count = count == null ? 0 : count;

		return new PageImpl<>(content, super.getValidPageable(pageable), count);
	}


	@Override
	public void removePost(Long postId, Long userId) {
		// 댓글들을 먼저 소프트 삭제 (isDeleted 필드를 true로 설정)
		jpaQueryFactory.update(comment)
				.set(comment.isDeleted, true)
				.where(comment.post.id.eq(postId)
						.and(comment.isDeleted.eq(false))
						.and(comment.users.id.eq(userId)))
				.execute();

		// 게시글을 소프트 삭제 (isDeleted 필드를 true로 설정)
		jpaQueryFactory.update(post)
				.set(post.isDeleted, true)
				.where(post.id.eq(postId)
						.and(post.isDeleted.eq(false))
						.and(post.users.id.eq(userId)))
				.execute();
	}

	public void increaseLikeCount(Long postId, Boolean status) {
		jpaQueryFactory
				.update(post)
				.set(post.likeCount, post.likeCount.add(status ? -1 : 1))
				.where(post.id.eq(postId))
				.execute();
	}
}

