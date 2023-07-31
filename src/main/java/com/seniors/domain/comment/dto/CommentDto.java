package com.seniors.domain.comment.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.annotations.QueryProjection;
import com.seniors.domain.comment.entity.Comment;
import com.seniors.domain.post.dto.PostDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@ToString
@Getter
public class CommentDto {

	@Data
	@Builder
	public static class CommentCreateDto {

		@Schema(description = "댓글 내용", defaultValue = "내용 1", example = "내용 1이요")
		@NotBlank(message = "Input content!")
		private String content;

		@Schema(description = "작성자 ID")
		@NotBlank(message = "Required userId")
		@JsonIgnore
		private Long userId;

		public static CommentDto.CommentCreateDto of(String content, Long userId) {
			return CommentDto.CommentCreateDto.builder().content(content).userId(userId).build();
		}
	}

	@Data
	public static class GetCommentRes {
		@Schema(description = "댓글 ID")
		private Long commentId;

		@Schema(description = "댓글 내용", defaultValue = "댓글 내용 1", example = "댓글 내용 1이요")
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

	@Data
	public static class SaveCommentDto extends Comment {
		@JsonIgnore
		private Long userId;
	}

	@Data
	public static class ModifyCommentDto extends Comment {
		@JsonIgnore
		private Long userId;
	}
}
