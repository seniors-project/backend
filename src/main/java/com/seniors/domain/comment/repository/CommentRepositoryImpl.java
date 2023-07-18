package com.seniors.domain.comment.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seniors.common.repository.BasicRepoSupport;
import com.seniors.domain.comment.entity.QComment;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import static com.seniors.domain.comment.dto.CommentDto.ModifyCommentDto;

@Repository
public class CommentRepositoryImpl extends BasicRepoSupport implements CommentRepositoryCustom {

    private static final QComment comment = QComment.comment;
    protected CommentRepositoryImpl(JPAQueryFactory jpaQueryFactory, EntityManager em) {
        super(jpaQueryFactory, em);
    }

    @Override
    public void modifyComment(Long commentId, ModifyCommentDto modifyCommentDto, Long userId) {
        jpaQueryFactory.update(comment)
                .set(comment.content, modifyCommentDto.getContent())
                .where(comment.id.eq(commentId)
                        .and(comment.users.id.eq(userId))
                )
                .execute();
    }

    @Override
    public void removeComment(Long commentId, Long userId) {
        jpaQueryFactory.delete(comment)
                .where(comment.id.eq(commentId).and(comment.users.id.eq(userId)))
                .execute();
    }
}
