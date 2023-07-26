package com.seniors.domain.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Getter
@Builder
public class PostLikeDto {

    @Data
    public static class SetLikeDto {
        @Schema(description = "현재 좋아요 상태")
        private Integer status;
    }
}
