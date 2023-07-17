package com.seniors.domain.post.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.annotations.QueryProjection;
import com.seniors.domain.comment.entity.Comment;
import com.seniors.domain.post.entity.Post;
import com.seniors.domain.users.dto.UsersDto.GetPostUserRes;
import com.seniors.domain.users.entity.Users;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.seniors.domain.comment.dto.CommentDto.GetCommentRes;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Getter
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

		@Schema(description = "작성자 ID")
		@NotBlank(message = "Required userId")
		@JsonIgnore
		private Long userId;

		public static PostCreateDto of(String title, String content, Long userId) {
			return PostCreateDto.builder().title(title).content(content).userId(userId).build();
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

		@Schema(description = "작성자")
		private GetPostUserRes users;

		@Schema(description = "게시글 댓글 리스트")
		private List<GetCommentRes> comments; // Update the field type to List<GetCommentRes>

		public GetPostRes(Long postId, String title, String content, Integer viewCount,
		                  LocalDateTime createdAt, LocalDateTime lastModifiedDate, Users users,
		                  List<Comment> comments) {
			this.postId = postId;
			this.title = title;
			this.content = content;
			this.viewCount = viewCount;
			this.createdAt = createdAt;
			this.lastModifiedDate = lastModifiedDate;
			this.users = new GetPostUserRes(
					users.getId(),
					users.getGender(),
					users.getNickname(),
					users.getProfileImageUrl()
			);
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

	@Data
	public static class SavePostReq extends Post {
		@JsonIgnore
		private Long userId;
	}

	@Data
	public static class ModifyPostReq extends Post {
		@JsonIgnore
		private Long userId;
	}

}
