package com.seniors.domain.post.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.seniors.domain.comment.dto.CommentDto;
import com.seniors.domain.comment.entity.Comment;
import com.seniors.domain.post.entity.Post;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.seniors.domain.comment.dto.CommentDto.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Getter
@Setter
public class PostDto {

	@Data
	@Builder
	public static class PostCreateDto {
		@Schema(description = "게시글 제목", defaultValue = "제목 1", example = "제목 1이요")
		@NotBlank(message = "Input title!")
		private String title;

		@Schema(description = "게시글 내용", defaultValue = "내용 1", example = "내용 1이요")
		@NotBlank(message = "Input content!")
		private String content;
		public static PostCreateDto of(String title, String content) {
			return PostCreateDto.builder().title(title).content(content).build();
		}
	}

	@Data
	public static class GetPostRes {
		@Schema(description = "게시글 ID")
		private Long postId;

		@Schema(description = "게시글 제목", defaultValue = "제목 1", example = "제목 1이요")
		private String title;

		@Schema(description = "게시글 내용", defaultValue = "내용 1", example = "내용 1이요")
		private String content;

		@Schema(description = "조회 수")
		private Integer viewCount;

		@Schema(description = "생성 일자")
		private LocalDateTime createdAt;

		@Schema(description = "최근 수정 일자")
		private LocalDateTime lastModifiedDate;

		@Schema(description = "게시글 댓글 리스트")
		private List<GetCommentRes> comments; // Update the field type to List<GetCommentRes>

		@QueryProjection
		public GetPostRes(Long postId, String title, String content, Integer viewCount,
						  LocalDateTime createdAt, LocalDateTime lastModifiedDate, List<Comment> comments) {
			this.postId = postId;
			this.title = title;
			this.content = content;
			this.viewCount = viewCount;
			this.createdAt = createdAt;
			this.lastModifiedDate = lastModifiedDate;
			this.comments = comments.stream() // Map Comment objects to GetCommentRes objects
					.map(comment -> new GetCommentRes(
							comment.getId(),
							comment.getContent(),
							comment.getCreatedAt(),
							comment.getLastModifiedDate()
					))
					.collect(Collectors.toList());
		}
	}

}
