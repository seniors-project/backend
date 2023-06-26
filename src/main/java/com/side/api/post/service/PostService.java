package com.side.api.post.service;

import com.side.api.post.domain.Post;
import com.side.api.post.dto.PostDto;
import com.side.api.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

	private final PostRepository postRepository;

	public void write(PostDto.Post postDto) {
		postRepository.save(Post.initPost(postDto.getTitle(), postDto.getContent(), 1));
	}
}
