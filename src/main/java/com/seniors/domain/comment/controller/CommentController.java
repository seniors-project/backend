package com.seniors.domain.comment.controller;

import com.seniors.common.annotation.LoginUsers;
import com.seniors.common.dto.DataResponseDto;
import com.seniors.common.dto.ErrorResponse;
import com.seniors.common.exception.type.BadRequestException;
import com.seniors.common.exception.type.NotAuthorizedException;
import com.seniors.common.exception.type.NotFoundException;
import com.seniors.config.security.CustomUserDetails;
import com.seniors.domain.comment.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static com.seniors.domain.comment.dto.CommentDto.CommentCreateDto;

@Tag(name = "댓글", description = "댓글 API 명세서")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 생성")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "생성 요청 body",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommentCreateDto.class)))
    @ApiResponse(responseCode = "200", description = "생성 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponseDto.class)))
    @ApiResponse(responseCode = "400", description = "유효성 검증 실패",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)))
    @ApiResponse(responseCode = "401", description = "유효하지 않은 회원입니다.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = NotAuthorizedException.class)))
    @ApiResponse(responseCode = "404", description = "객체 유효성 검증 실패",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = NotFoundException.class)))
    @ApiResponse(responseCode = "500", description = "생성 실패",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping("")
    public DataResponseDto<String> commentAdd(
            @RequestBody @Valid CommentCreateDto commentDto,
            @RequestParam(value = "postId") Long postId,
            @LoginUsers CustomUserDetails userDetails) {
        commentService.addComment(commentDto, postId, userDetails.getUserId());
        return DataResponseDto.of("SUCCESS");
    }

    @Operation(summary = "댓글 수정")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "수정 요청 body",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommentCreateDto.class)))
    @ApiResponse(responseCode = "200", description = "수정 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponseDto.class)))
    @ApiResponse(responseCode = "400", description = "유효성 검증 실패",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)))
    @ApiResponse(responseCode = "401", description = "유효하지 않은 회원입니다.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = NotAuthorizedException.class)))
    @ApiResponse(responseCode = "404", description = "객체 유효성 검증 실패",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = NotFoundException.class)))
    @ApiResponse(responseCode = "500", description = "수정 실패",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    @PatchMapping("/{commentId}")
    public DataResponseDto<String> commentModify(
            @Parameter(description = "댓글 ID") @PathVariable(value = "commentId") Long commentId,
            @RequestBody @Valid CommentCreateDto commentDto,
            @LoginUsers CustomUserDetails userDetails) {
        commentService.modifyComment(commentId, commentDto, userDetails.getUserId());
        return DataResponseDto.of("SUCCESS");
    }

    @Operation(summary = "댓글 삭제")
    @ApiResponse(responseCode = "200", description = "삭제 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponseDto.class)))
    @ApiResponse(responseCode = "400", description = "유효성 검증 실패",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)))
    @ApiResponse(responseCode = "401", description = "유효하지 않은 회원입니다.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = NotAuthorizedException.class)))
    @ApiResponse(responseCode = "404", description = "객체 유효성 검증 실패",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = NotFoundException.class)))
    @ApiResponse(responseCode = "500", description = "삭제 실패",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    @DeleteMapping("/{commentId}")
    public DataResponseDto<String> commentRemove(
            @Parameter(description = "댓글 ID") @PathVariable(value = "commentId") Long commentId,
            @LoginUsers CustomUserDetails userDetails
    ) {
        commentService.removeComment(commentId, userDetails.getUserId());
        return DataResponseDto.of("SUCCESS");
    }
}
