package com.seniors.api.post.controller;

import com.seniors.api.common.dto.DataResponseDto;
import com.seniors.api.post.domain.Post;
import com.seniors.api.post.dto.PostDto;
import com.seniors.api.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostController {

	private final PostService postService;

	@PostMapping("")
	public DataResponseDto<String> postAdd(@Valid @RequestBody PostDto.Post postDto) {
		postService.addPost(postDto);
		return DataResponseDto.of("SUCCESS");
	}

	@GetMapping("/{postId}")
	public DataResponseDto<Post> postDetails(@PathVariable Long postId) {
		Post post = postService.findPost(postId);
		return DataResponseDto.of(post);
	}
}
