package com.seniors.domain.comment.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@ToString
@Getter
public class CommentDto {

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

		@QueryProjection
		public GetCommentRes(Long commentId, String content, LocalDateTime createdAt, LocalDateTime lastModifiedDate) {
			this.commentId = commentId;
			this.content = content;
			this.createdAt = createdAt;
			this.lastModifiedDate = lastModifiedDate;
		}
	}

	@Data
	public static class SaveCommentDto {
		@Schema(description = "댓글 내용", defaultValue = "댓글 내용 1", example = "댓글 내용 1이요")
		private String content;

		@Schema(description = "작성할 글 ID")
		private Long postId;

		@Schema(description = "작성한 사용자 ID")
		private Long userId;

//		@QueryProjection
//		public SaveCommentDto(String content, Long postId, Long userId) {
//			this.content = content;
//			this.postId = postId;
//			this.userId = userId;
//		}
	}

	@Data
	public static class ModifyCommentDto {
		@Schema(description = "댓글 ID")
		private Long commentId;

		@Schema(description = "댓글 내용", defaultValue = "댓글 내용 1", example = "댓글 내용 1이요")
		private String content;

		@Schema(description = "작성할 글 ID")
		private Long postId;

		@Schema(description = "작성한 사용자 ID")
		private Long userId;

//		@QueryProjection
//		public ModifyCommentDto(Long commentId, String content, Long postId, Long userId) {
//			this.commentId = commentId;
//			this.content = content;
//			this.postId = postId;
//			this.userId = userId;
//		}
	}
}
