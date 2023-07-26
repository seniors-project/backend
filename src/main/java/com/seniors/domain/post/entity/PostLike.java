package com.seniors.domain.post.entity;

import com.seniors.domain.users.entity.Users;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@IdClass(PostLikeEmbedded.class) // Specify the composite primary key class
public class PostLike {

    @Id
    @Column(name = "postId")
    private Long postId;

    @Id
    @Column(name = "userId")
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postId", insertable = false, updatable = false) // Define the correct column name
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", insertable = false, updatable = false) // Define the correct column name
    private Users users;

    @Column(columnDefinition = "int unsigned not null default 0 COMMENT '게시글 좋아요 상태'")
    private Integer status;

    public static PostLike from(Long postId, Long userId, Integer status) {
        return PostLike.builder()
                .postId(postId)
                .userId(userId)
                .status(status)
                .build();
    }

    @Builder
    public PostLike(Long postId, Long userId,  Integer status) {
        this.postId = postId;
        this.userId = userId;
        this.status = status;
    }
}
