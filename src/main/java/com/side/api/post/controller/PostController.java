package com.side.api.post.controller;

import com.side.api.post.dto.PostDto;
import com.side.api.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostController {

	private final PostService postService;

	@PostMapping("/")
	public ResponseEntity<String> postAdd(@Valid @RequestBody PostDto.Post postDto) {
		postService.addPost(postDto);
		return ResponseEntity.ok().body("SUCCESS");
	}
}
