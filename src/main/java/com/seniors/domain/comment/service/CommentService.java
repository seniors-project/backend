package com.seniors.domain.comment.service;

import com.seniors.common.exception.type.BadRequestException;
import com.seniors.domain.comment.dto.CommentDto;
import com.seniors.domain.comment.dto.CommentDto.ModifyCommentDto;
import com.seniors.domain.comment.dto.CommentDto.SaveCommentDto;
import com.seniors.domain.comment.entity.Comment;
import com.seniors.domain.comment.repository.CommentRepository;
import com.seniors.domain.post.entity.Post;
import com.seniors.domain.post.repository.PostRepository;
import com.seniors.domain.users.entity.Users;
import com.seniors.domain.users.repository.UsersRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UsersRepository usersRepository;

    @Transactional
    public void addComment(SaveCommentDto commentReq, Long postId, Long userId) {
        if (commentReq.getContent() == null || commentReq.getContent().isEmpty()) {
            throw new BadRequestException("Content is required");
        }
        Post post = postRepository.findById(postId).orElse(null);
        usersRepository.findById(userId).ifPresent(users ->
                commentRepository.save(Comment.of(commentReq.getContent(), post, users))
        );
    }

    @Transactional
    public void modifyComment(Long commentId, ModifyCommentDto modifyCommentDto, Long userId) {
        commentRepository.modifyComment(commentId, modifyCommentDto, userId);
    }

    @Transactional
    public void removeComment(Long commentId, Long userId) {
        commentRepository.removeComment(commentId, userId);
    }
}
