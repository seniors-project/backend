package com.seniors.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthTokens {
	private String accessToken;
	private String refreshToken;
	private String grantType;
	private Long expiresIn;

	public static AuthTokens of(String accessToken, String refreshToken, String grantType, Long expiresIn) {
		return new AuthTokens(accessToken, refreshToken, grantType, expiresIn);
	}
}
