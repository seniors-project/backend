package com.seniors.domain.post.service;

import com.seniors.domain.post.entity.Post;
import com.seniors.domain.post.dto.PostDto;
import com.seniors.domain.post.repository.PostRepository;
import com.seniors.domain.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

	private final PostRepository postRepository;
	private final UsersRepository usersRepository;

	public void addPost(PostDto.Post postDto, Long userId) {
		usersRepository.findById(userId).ifPresent(users ->
				postRepository.save(Post.of(postDto.getTitle(), postDto.getContent(), users))
		);
	}

	@Transactional
	public void removePost(Long postId) {
		postRepository.deleteById(postId);
	}

	@Transactional
	public Post findPost(Long postId) {
		return postRepository.getOnePost(postId);
	}
}
