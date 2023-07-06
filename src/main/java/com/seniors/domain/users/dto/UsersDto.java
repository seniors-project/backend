package com.seniors.domain.users.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@ToString
@Getter
public class UsersDto {

	@Data
	@Builder
	public static class GetUserRes {
		@Schema(description = "사용자 ID")
		private Long userId;

		@Schema(description = "sns ID")
		private String snsId;

		@Schema(description = "이메일")
		private String email;

		@Schema(description = "닉네임")
		private String nickname;

		@Schema(description = "성별")
		private String gender;

		@Schema(description = "프로필 이미지 url")
		private String profileImageUrl;

		@Schema(description = "생성 일자")
		private LocalDateTime createdAt;

		@Schema(description = "최근 수정 일자")
		private LocalDateTime lastModifiedDate;

	}
}
