package com.seniors.domain.comment.repository;

import com.seniors.domain.comment.dto.CommentDto.CommentCreateDto;

public interface CommentRepositoryCustom {
    void modifyComment(Long commentId, CommentCreateDto modifyCommentDto, Long userId);

    void removeComment(Long commentId, Long userId);
}
