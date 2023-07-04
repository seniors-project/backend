package com.seniors.domain.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@NoArgsConstructor
@ToString
@Getter
public class PostDto {

	@Data
	public static class Post {
		@Schema(description = "글 제목", defaultValue = "제목 1", example = "제목 1이요")
		@NotBlank
		private String title;

		@Schema(description = "글 내용", defaultValue = "내용 1", example = "내용 1이요")
		@NotBlank
		private String content;
	}
}
