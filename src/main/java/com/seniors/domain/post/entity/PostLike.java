package com.seniors.domain.post.entity;

import com.seniors.domain.users.entity.Users;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PostLike {

    @EmbeddedId
    private PostLikeId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn(name = "postId")
    private Post postId;

    @ManyToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn(name = "userId")
    private Users users;

    @Column(columnDefinition = "int unsigned not null default 0 COMMENT '게시글 좋아요 상태'")
    private Integer status;
}

