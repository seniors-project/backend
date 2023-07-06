package com.seniors.domain.comment.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@ToString
@Getter
public class CommentDto {

	@Data
	@Builder
	public static class GetCommentRes {
		@Schema(description = "댓글 ID")
		@NotBlank
		private Long commentId;

		@Schema(description = "댓글 내용", defaultValue = "내용 1", example = "내용 1이요")
		@NotBlank
		private String content;

		private LocalDateTime createdAt;

		private LocalDateTime lastModifiedDate;

		public GetCommentRes(Long commentId, String content, LocalDateTime createdAt, LocalDateTime lastModifiedDate) {
			this.commentId = commentId;
			this.content = content;
			this.createdAt = createdAt;
			this.lastModifiedDate = lastModifiedDate;
		}
	}
}
