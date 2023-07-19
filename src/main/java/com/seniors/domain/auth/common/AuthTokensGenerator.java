package com.seniors.domain.auth.common;

import com.seniors.config.security.TokenService;
import com.seniors.domain.auth.dto.AuthTokens;
import com.seniors.domain.users.entity.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class AuthTokensGenerator {
	private static final String BEARER_TYPE = "Bearer";
	private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60
			* 60 * 24;            // 1일
	private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60
			* 60 * 24 * 7;  // 7일

	private final JwtTokenProvider jwtTokenProvider;
	private final TokenService tokenService;

	public AuthTokens generate(Users user) {
		final LocalDateTime now = LocalDateTime.now();
		final Duration expiresIn = Duration.between(now, now.plusMinutes(ACCESS_TOKEN_EXPIRE_TIME));
		final Duration refreshExpiresIn =
				Duration.between(now, now.plusMinutes(REFRESH_TOKEN_EXPIRE_TIME));

		return AuthTokens.builder()
				.accessToken(tokenService.generateToken(user, ACCESS_TOKEN_EXPIRE_TIME))
				.refreshToken(tokenService.generateRefreshToken(user, REFRESH_TOKEN_EXPIRE_TIME))
				.grantType(BEARER_TYPE)
				.expiresIn(ACCESS_TOKEN_EXPIRE_TIME / 1000L).build();
	}

	public Long extractUserId(String accessToken) {
		return Long.valueOf(jwtTokenProvider.extractSubject(accessToken));
	}
}
