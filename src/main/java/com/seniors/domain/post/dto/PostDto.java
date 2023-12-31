package com.seniors.domain.post.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.seniors.domain.comment.entity.Comment;
import com.seniors.domain.post.entity.Post;
import com.seniors.domain.post.entity.PostMedia;
import com.seniors.domain.users.dto.UsersDto.GetPostUserRes;
import com.seniors.domain.users.entity.Users;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.seniors.domain.comment.dto.CommentDto.GetCommentRes;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Getter
public class PostDto {

	@Data
	public static class SetPostDto {
		@NotEmpty(message = "게시글 제목은 비워둘 수 없습니다.")
		@Schema(description = "게시글 제목", defaultValue = "제목 1", example = "제목 1이요")
		private String title;

		@NotEmpty(message = "게시글 내용은 비워둘 수 없습니다.")
		@Schema(description = "게시글 내용", defaultValue = "내용 1", example = "내용 1이요")
		private String content;
	}

	@Data
	@Builder
	public static class PostCreateDto extends SetPostDto {
		@NotEmpty(message = "게시글 제목은 비워둘 수 없습니다.")
		@Schema(description = "게시글 제목", defaultValue = "제목 1", example = "제목 1이요")
		private String title;

		@NotEmpty(message = "게시글 내용은 비워둘 수 없습니다.")
		@Schema(description = "게시글 내용", defaultValue = "내용 1", example = "내용 1이요")
		private String content;

		public static SetPostDto of(String title, String content) {
			return PostCreateDto.builder()
					.title(title)
					.content(content)
					.build();
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

		@Schema(description = "게시글 좋아요 상태")
		private Boolean likeStatus;

		@Schema(description = "생성 일자")
		private LocalDateTime createdAt;

		@Schema(description = "최근 수정 일자")
		private LocalDateTime lastModifiedDate;

		@Schema(description = "작성자")
		private GetPostUserRes users;

		@Schema(description = "게시글 댓글 리스트")
		private List<GetCommentRes> comments; // Update the field type to List<GetCommentRes>

		@Schema(description = "게시글 이미지 및 동영상")
		private Set<GetPostMediaRes> postMedias;

		public GetPostRes(Long postId, String title, String content, Boolean likeStatus,
		                  LocalDateTime createdAt, LocalDateTime lastModifiedDate, Users users,
		                  List<Comment> comments, Set<PostMedia> postMedias) {
			this.postId = postId;
			this.title = title;
			this.content = content;
			this.likeStatus = likeStatus;
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
			this.postMedias = postMedias.stream()
					.map(postMedia -> new GetPostMediaRes(
							postMedia.getId(),
							postMedia.getMediaUrl(),
							postMedia.getCreatedAt(),
							postMedia.getLastModifiedDate()
					))
					.collect(Collectors.toSet());
		}
	}

	@Data
	public static class GetPostMediaRes {
		@Schema(description = "게시글 미디어 ID")
		private Long postMediaId;

		@Schema(description = "게시글 미디어 url")
		private String mediaUrl;

		@Schema(description = "생성 일자")
		private LocalDateTime createdAt;

		@Schema(description = "최근 수정 일자")
		private LocalDateTime lastModifiedDate;

		public GetPostMediaRes(Long postMediaId, String mediaUrl, LocalDateTime createdAt, LocalDateTime lastModifiedDate) {
			this.postMediaId = postMediaId;
			this.mediaUrl = mediaUrl;
			this.createdAt = createdAt;
			this.lastModifiedDate = lastModifiedDate;
		}
	}

	@Data
	public static class GetPostLikeRes {
		@Schema(description = "좋아요 상태")
		private Boolean status;

		public GetPostLikeRes(Boolean status) {
			this.status = status;
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
