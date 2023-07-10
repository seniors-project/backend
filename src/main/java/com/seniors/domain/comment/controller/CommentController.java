package com.seniors.domain.comment.controller;

import com.seniors.common.dto.DataResponseDto;
import com.seniors.domain.comment.dto.CommentDto;
import com.seniors.domain.comment.entity.Comment;
import com.seniors.domain.comment.service.CommentService;
import io.swagger.v3.oas.annotations.Parameter;
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

    @PostMapping("")
    public DataResponseDto<String> commentAdd(@RequestBody @Valid SaveCommentDto commentDto) {
        Comment comment = commentService.addComment(commentDto);
        return DataResponseDto.of("Comment [" + comment.getId() + "] add success");
    }

    @PatchMapping("/{commentId}")
    public DataResponseDto<String> commentModify(
            @Parameter(description = "댓글 ID") @PathVariable(value = "commentId") Long commentId,
            @RequestBody @Valid ModifyCommentDto commentDto) {
        commentService.modifyComment(commentId, commentDto);
        return DataResponseDto.of("Comment [" + commentId + "] modify success");
    }

    @DeleteMapping("/{commentId}")
    public DataResponseDto<String> commentRemove(
            @Parameter(description = "댓글 ID") @PathVariable(value = "commentId") Long commentId
    ) {
        commentService.removeComment(commentId);
        return DataResponseDto.of("Comment [" + commentId + "] delete success");
    }
}
