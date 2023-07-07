package com.seniors.domain.users.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Getter
@Setter
public class UsersDto {

	@Data
	public static class GetPostUserRes {
		@Schema(description = "사용자 ID")
		private Long userId;

		@Schema(description = "성별")
		private String gender;

		@Schema(description = "닉네임")
		private String nickname;

		@Schema(description = "프로필 이미지 url")
		private String profileImageUrl;

		@QueryProjection
		public GetPostUserRes(Long userId, String gender, String nickname, String profileImageUrl) {
			this.userId = userId;
			this.gender = gender;
			this.nickname = nickname;
			this.profileImageUrl = profileImageUrl;
		}
	}
}
