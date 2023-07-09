package com.seniors.domain.post.service;

import com.seniors.common.exception.type.BadRequestException;
import com.seniors.domain.post.dto.PostDto.GetPostRes;
import com.seniors.domain.post.entity.Post;
import com.seniors.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

	private final PostRepository postRepository;

	public void addPost(Post postReq) {
		if (postReq.getTitle() == null || postReq.getTitle().isEmpty() || postReq.getContent() == null || postReq.getContent().isEmpty()) {
			throw new BadRequestException("Title or Content is required");
		}
		postRepository.save(Post.of(postReq.getTitle(), postReq.getContent()));
	}

	@Transactional
	public void removePost(Long postId) {
		postRepository.deleteById(postId);
	}

	@Transactional
	public GetPostRes findPost(Long postId) {
		return postRepository.getOnePost(postId);
	}
}
