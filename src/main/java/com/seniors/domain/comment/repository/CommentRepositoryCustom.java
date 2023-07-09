package com.seniors.domain.comment.repository;

import com.seniors.domain.comment.dto.CommentDto;
import com.seniors.domain.comment.dto.CommentDto.ModifyCommentDto;

public interface CommentRepositoryCustom {
    void modifyComment(Long commentId, ModifyCommentDto modifyCommentDto);
}
