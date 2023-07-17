package com.seniors.domain.post.service;

import com.seniors.common.dto.CustomPage;
import com.seniors.common.exception.type.BadRequestException;
import com.seniors.domain.post.dto.PostDto.GetPostRes;
import com.seniors.domain.post.dto.PostDto.ModifyPostReq;
import com.seniors.domain.post.dto.PostDto.SavePostReq;
import com.seniors.domain.post.entity.Post;
import com.seniors.domain.post.repository.PostRepository;
import com.seniors.domain.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

	private final PostRepository postRepository;
	private final UsersRepository usersRepository;

	@Transactional
	public void addPost(SavePostReq postReq, Long userId) {
		if (postReq.getTitle() == null || postReq.getTitle().isEmpty() || postReq.getContent() == null || postReq.getContent().isEmpty()) {
			throw new BadRequestException("Title or Content is required");
		}

		usersRepository.findById(userId).ifPresent(users ->
				postRepository.save(Post.of(postReq.getTitle(), postReq.getContent(), users))
		);
	}

	@Transactional(readOnly = true)
	public GetPostRes findOnePost(Long postId, Long userId) {
		return postRepository.findOnePost(postId, userId);
	}

	@Transactional(readOnly = true)
	public CustomPage<GetPostRes> findPost(int page, int size) {
		Direction direction = Direction.DESC;
		Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "id"));
		Page<GetPostRes> posts = postRepository.findAllPost(pageable);
		return CustomPage.of(posts);
	}

	@Transactional
	public void modifyPost(ModifyPostReq modifyPostReq, Long postId, Long userId) {

		postRepository.modifyPost(modifyPostReq, postId, userId);
	}

	@Transactional
	public void removePost(Long postId) {
		postRepository.deleteById(postId);
	}

}
