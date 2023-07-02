package com.seniors.api.post.controller;

import com.seniors.api.common.dto.DataResponseDto;
import com.seniors.api.post.domain.Post;
import com.seniors.api.post.dto.PostDto;
import com.seniors.api.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Tag(name = "게시글", description = "게시글 API 명세서")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostController {

	private final PostService postService;

	@Operation(summary = "게시글 생성")
	@PostMapping("")
	public DataResponseDto<String> postAdd(@Valid @RequestBody PostDto.Post postDto) {
		postService.addPost(postDto);
		return DataResponseDto.of("SUCCESS");
	}

	@Operation(summary = "게시글 단건 조회")
	@GetMapping("/{postId}")
	public DataResponseDto<Post> postDetails(@Parameter(description = "게시글 ID") @PathVariable Long postId) {
		Post post = postService.findPost(postId);
		return DataResponseDto.of(post);
	}

	@Operation(summary = "게시글 단건 삭제")
	@DeleteMapping("/{postId}")
	public DataResponseDto<String> postRemove(@PathVariable Long postId) {
		postService.removePost(postId);
		return DataResponseDto.of("SUCCESS");
	}
}
