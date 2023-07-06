package com.seniors.domain.comment.dto;

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
		private Long commentId;

		@Schema(description = "댓글 내용", defaultValue = "댓글 내용 1", example = "댓글 내용 1이요")
		@NotBlank(message = "Input content!")
		private String content;

		@Schema(description = "생성 일자")
		private LocalDateTime createdAt;

		@Schema(description = "최근 수정 일자")
		private LocalDateTime lastModifiedDate;
		
	}
}
