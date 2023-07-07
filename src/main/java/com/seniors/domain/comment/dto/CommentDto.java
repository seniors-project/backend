package com.seniors.domain.comment.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Getter
@Setter
public class CommentDto {

	@Data
	public static class GetCommentRes {
		@Schema(description = "댓글 ID")
		private Long commentId;

		@Schema(description = "댓글 내용", defaultValue = "댓글 내용 1", example = "댓글 내용 1이요")
		@NotBlank(message = "Input content!")
		private String content;

		@Schema(description = "생성 일자")
		private LocalDateTime createdAt;

		@Schema(description = "최근 수정 일자")
		private LocalDateTime lastModifiedDate;

		public GetCommentRes(Long commentId, String content, LocalDateTime createdAt, LocalDateTime lastModifiedDate) {
			this.commentId = commentId;
			this.content = content;
			this.createdAt = createdAt;
			this.lastModifiedDate = lastModifiedDate;
		}
	}
}
