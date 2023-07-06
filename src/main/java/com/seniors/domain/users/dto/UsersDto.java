package com.seniors.domain.users.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@ToString
@Getter
public class UsersDto {

	@Data
	public static class GetUserRes {
		private Long id;
		private String snsId;
		private String email;
		private String nickname;
		private String phoneNumber;
		private String profileImageUrl;

		@QueryProjection
		public GetUserRes(Long id, String snsId, String email, String nickname, String phoneNumber, String profileImageUrl) {
			this.id = id;
			this.snsId = snsId;
			this.email = email;
			this.nickname = nickname;
			this.phoneNumber = phoneNumber;
			this.profileImageUrl = profileImageUrl;
		}
	}

}
