package com.seniors.api.post.service;

import com.seniors.api.post.domain.Post;
import com.seniors.api.post.dto.PostDto;
import com.seniors.api.post.repository.PostRepository;
import com.seniors.api.users.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

	private final PostRepository postRepository;
	private final UsersRepository usersRepository;

	public void addPost(PostDto.Post postDto) {
		Users users = usersRepository.findById(userId).orElse(null);
		if (users != null) {
			postRepository.save(Post.initPost(postDto.getTitle(), postDto.getContent(), users));
		}
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
