package com.seniors.domain.auth.service;

import com.seniors.domain.auth.common.AuthTokensGenerator;
import com.seniors.domain.auth.common.OAuthInfoResponse;
import com.seniors.domain.auth.common.OAuthLoginParams;
import com.seniors.domain.auth.dto.AuthTokens;
import com.seniors.domain.users.entity.Users;
import com.seniors.domain.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthLoginService {
	private final UsersRepository usersRepository;
	private final AuthTokensGenerator authTokensGenerator;
	private final RequestOAuthInfoService requestOAuthInfoService;

	public AuthTokens login(OAuthLoginParams params) {
		OAuthInfoResponse oAuthInfoResponse = requestOAuthInfoService.request(params);
		Long userId = findOrCreateUser(oAuthInfoResponse);
		return authTokensGenerator.generate(userId);
	}

	private Long findOrCreateUser(OAuthInfoResponse oAuthInfoResponse) {
		return usersRepository.findByEmail(oAuthInfoResponse.getEmail())
				.map(Users::getId)
				.orElseGet(() -> signUp(oAuthInfoResponse));
	}

	private Long signUp(OAuthInfoResponse oAuthInfoResponse) {
		Users user = Users.builder()
				.snsId(oAuthInfoResponse.getSnsId())
				.email(oAuthInfoResponse.getEmail())
				.nickname(oAuthInfoResponse.getNickname())
				.oAuthProvider(oAuthInfoResponse.getOAuthProvider())
				.build();

		return usersRepository.save(user).getId();
	}
}
