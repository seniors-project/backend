package com.seniors.domain.auth.service;

import com.seniors.domain.auth.common.AuthTokensGenerator;
import com.seniors.domain.auth.common.OAuthInfoResponse;
import com.seniors.domain.auth.common.OAuthLoginParams;
import com.seniors.domain.auth.dto.AuthTokens;
import com.seniors.domain.users.entity.Users;
import com.seniors.domain.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuthLoginService {
	private final UsersRepository usersRepository;
	private final AuthTokensGenerator authTokensGenerator;
	private final RequestOAuthInfoService requestOAuthInfoService;

	public AuthTokens login(OAuthLoginParams params) {
		OAuthInfoResponse oAuthInfoResponse = requestOAuthInfoService.request(params);
		Users users = findOrCreateUser(oAuthInfoResponse);
		return authTokensGenerator.generate(users);
	}

	private Users findOrCreateUser(OAuthInfoResponse oAuthInfoResponse) {
		return usersRepository.findByNicknameAndSnsId(oAuthInfoResponse.getNickname(), oAuthInfoResponse.getSnsId())
				.orElseGet(() -> signUp(oAuthInfoResponse));
	}

	private Users signUp(OAuthInfoResponse oAuthInfoResponse) {
		Users user = Users.builder()
				.snsId(oAuthInfoResponse.getSnsId())
				.email(oAuthInfoResponse.getEmail())
				.nickname(oAuthInfoResponse.getNickname())
				.oAuthProvider(oAuthInfoResponse.getOAuthProvider())
				.gender(oAuthInfoResponse.getGender())
				.birthday(oAuthInfoResponse.getBirthday())
				.ageRange(oAuthInfoResponse.getAgeRange())
				.profileImageUrl(oAuthInfoResponse.getProfileImageUrl())
				.build();

		return usersRepository.save(user);
	}
}
