package com.seniors.domain.post.dto;

import com.seniors.domain.post.entity.Post;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@NoArgsConstructor
@ToString
@Getter
public class PostDto {

	@Data
	@Builder
	public static class PostCreateDto {
		@Schema(description = "글 제목", defaultValue = "제목 1", example = "제목 1이요")
		@NotBlank(message = "Input title!")
		private String title;

		@Schema(description = "글 내용", defaultValue = "내용 1", example = "내용 1이요")
		@NotBlank(message = "Input content!")
		private String content;
		public static PostCreateDto of(String title, String content) {
			return PostCreateDto.builder().title(title).content(content).build();
		}
	}
}
