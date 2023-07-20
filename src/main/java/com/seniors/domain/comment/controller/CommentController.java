package com.seniors.domain.comment.controller;

import com.seniors.common.annotation.LoginUsers;
import com.seniors.common.dto.DataResponseDto;
import com.seniors.config.security.CustomUserDetails;
import com.seniors.domain.comment.dto.CommentDto;
import com.seniors.domain.comment.entity.Comment;
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

import static com.seniors.domain.comment.dto.CommentDto.*;

@Tag(name = "댓글", description = "댓글 API 명세서")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 생성")
    @ApiResponse(responseCode = "200", description = "생성 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponseDto.class)))
    @PostMapping("")
    public DataResponseDto<String> commentAdd(
            @RequestBody @Valid SaveCommentDto commentDto,
            @RequestParam(value = "postId") Long postId,
            @LoginUsers CustomUserDetails userDetails) {
        commentService.addComment(commentDto, postId, userDetails.getUserId());
        return DataResponseDto.of("SUCCESS");
    }

    @Operation(summary = "댓글 수정")
    @ApiResponse(responseCode = "200", description = "수정 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponseDto.class)))
    @PatchMapping("/{commentId}")
    public DataResponseDto<String> commentModify(
            @Parameter(description = "댓글 ID") @PathVariable(value = "commentId") Long commentId,
            @RequestBody @Valid ModifyCommentDto commentDto,
            @LoginUsers CustomUserDetails userDetails) {
        commentService.modifyComment(commentId, commentDto, userDetails.getUserId());
        return DataResponseDto.of("SUCCESS");
    }

    @Operation(summary = "댓글 삭제")
    @ApiResponse(responseCode = "200", description = "삭제 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponseDto.class)))
    @DeleteMapping("/{commentId}")
    public DataResponseDto<String> commentRemove(
            @Parameter(description = "댓글 ID") @PathVariable(value = "commentId") Long commentId,
            @LoginUsers CustomUserDetails userDetails
    ) {
        commentService.removeComment(commentId, userDetails.getUserId());
        return DataResponseDto.of("SUCCESS");
    }
}