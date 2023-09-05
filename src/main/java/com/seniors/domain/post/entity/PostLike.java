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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postId", insertable = false, updatable = false) // Define the correct column name
    private Post post;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", insertable = false, updatable = false) // Define the correct column name
    private Users users;

    @Column(name = "postId")
    private Long postId;

    @Column(name = "userId")
    private Long userId;

    @Column(columnDefinition = "bit(1) not null default 0 COMMENT '게시글 좋아요 상태'")
    private Boolean status;

    public static PostLike of(Boolean status, Post post, Users users) {
        return PostLike.builder()
                .status(status)
                .post(post)
                .users(users)
                .build();
    }

    @Builder
    public PostLike(Boolean status, Post post, Users users) {
        this.post = post;
        this.users = users;
        this.status = status;
    }
}
