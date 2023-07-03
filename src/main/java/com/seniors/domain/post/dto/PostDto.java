package com.seniors.domain.post.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@NoArgsConstructor
@ToString
@Getter
public class PostDto {

	@Data
	public static class Post {
		@NotBlank
		private String title;

		@NotBlank
		private String content;
	}
}
