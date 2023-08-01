package com.seniors.domain.post.controller;

import com.seniors.common.annotation.LoginUsers;
import com.seniors.common.dto.CustomPage;
import com.seniors.common.dto.DataResponseDto;
import com.seniors.common.dto.ErrorResponse;
import com.seniors.config.security.CustomUserDetails;
import com.seniors.domain.post.dto.PostDto.GetPostRes;
import com.seniors.domain.post.dto.PostDto.PostCreateDto;
import com.seniors.domain.post.dto.PostLikeDto.SetLikeDto;
import com.seniors.domain.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Tag(name = "게시글", description = "게시글 API 명세서")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

	private final PostService postService;

	@Operation(summary = "게시글 생성")
	@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "생성 요청 body",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostCreateDto.class)))
	@ApiResponse(responseCode = "200", description = "생성 성공",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponseDto.class)))
	@ApiResponse(responseCode = "500", description = "생성 실패",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
	@PostMapping("")
	public DataResponseDto<String> postAdd(
			@ModelAttribute @Valid PostCreateDto postCreateDto, BindingResult bindingResult,
			@LoginUsers CustomUserDetails userDetails) throws IOException {
		postService.addPost(postCreateDto, bindingResult, userDetails.getUserId());
		return DataResponseDto.of("SUCCESS");
	}

	@Operation(summary = "게시글 단건 조회")
	@ApiResponse(responseCode = "200", description = "단건 조회 성공",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = GetPostRes.class)))
	@ApiResponse(responseCode = "500", description = "단건 조회 실패",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
	@GetMapping("/{postId}")
	public DataResponseDto<GetPostRes> postDetails(
			@Parameter(description = "게시글 ID") @PathVariable(value = "postId") Long postId) {
		GetPostRes post = postService.findOnePost(postId);
		return DataResponseDto.of(post);
	}

	@Operation(summary = "게시글 리스트 조회")
	@ApiResponse(responseCode = "200", description = "리스트 조회 성공",
			content = @Content(mediaType = "application/json", schema =
			@Schema(implementation = CustomPage.class)))
	@ApiResponse(responseCode = "500", description = "리스트 조회 실패",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
	@GetMapping("")
	public DataResponseDto<CustomPage<GetPostRes>> postList(
			@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false) int size
	) {
		CustomPage<GetPostRes> postResList = postService.findPost(page > 0 ? page - 1 : 0, size);
		return DataResponseDto.of(postResList);
	}

	@Operation(summary = "게시글 수정")
	@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "수정 요청 body",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostCreateDto.class)))
	@ApiResponse(responseCode = "200", description = "단건 수정 성공",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponseDto.class)))
	@ApiResponse(responseCode = "500", description = "단건 수정 실패",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
	@PatchMapping("/{postId}")
	public DataResponseDto<String> postModify(
			@Parameter(description = "게시글 ID") @PathVariable(value = "postId") Long postId,
			@ModelAttribute @Valid PostCreateDto postCreateDto, BindingResult bindingResult,
			@LoginUsers CustomUserDetails userDetails) throws IOException {
		postService.modifyPost(postCreateDto, bindingResult, postId, userDetails.getUserId());
		return DataResponseDto.of("SUCCESS");
	}

	@Operation(summary = "게시글 단건 삭제")
	@ApiResponse(responseCode = "200", description = "단건 삭제 성공",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponseDto.class)))
	@ApiResponse(responseCode = "500", description = "단건 삭제 실패",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
	@DeleteMapping("/{postId}")
	public DataResponseDto<String> postRemove(
			@Parameter(description = "게시글 ID") @PathVariable(value = "postId") Long postId,
			@LoginUsers CustomUserDetails userDetails
	) {
		postService.removePost(postId, userDetails.getUserId());
		return DataResponseDto.of("SUCCESS");
	}

	@Operation(summary = "게시글 좋아요")
	@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "좋아요 상태 값 body",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SetLikeDto.class)))
	@ApiResponse(responseCode = "200", description = "좋아요 성공",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponseDto.class)))
	@ApiResponse(responseCode = "500", description = "좋아요 실패",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
	@PostMapping("/like")
	public DataResponseDto<String> postLike(
			@Parameter(description = "게시글 ID") @RequestParam(name = "postId", value = "postId") Long postId,
			@RequestBody @Valid SetLikeDto likeDto,
			@LoginUsers CustomUserDetails userDetails
	) {
		postService.likePost(postId, userDetails.getUserId(), likeDto.getStatus());
		return DataResponseDto.of("SUCCESS");
	}

}
