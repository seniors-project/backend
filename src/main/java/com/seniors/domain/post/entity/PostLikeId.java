package com.seniors.domain.post.entity;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostLikeId implements Serializable {

    private Long postId;
    private Long userId;

    // equals, hashCode 메서드 등을 재정의할 수 있음
}
