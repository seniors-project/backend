package com.seniors.domain.auth.kakao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.seniors.domain.auth.common.OAuthInfoResponse;
import com.seniors.common.constant.OAuthProvider;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoInfoResponse implements OAuthInfoResponse {

	// https://kapi.kakao.com/v2/user/me 요청 결과 값
	@JsonProperty("kakao_account")
	private KakaoAccount kakaoAccount;

	@Getter
	@JsonIgnoreProperties(ignoreUnknown = true)
	static class KakaoAccount {
		private KakaoProfile profile;
		private String sub;
		private String email;
		private String gender;
	}

	@Getter
	@JsonIgnoreProperties(ignoreUnknown = true)
	static class KakaoProfile {
		private String nickname;
		private String profileImageUrl;
		private String gender;
	}

	@Override
	public String getSnsId() {
		return kakaoAccount.sub;
	}

	@Override
	public String getEmail() {
		return kakaoAccount.email;
	}

	@Override
	public String getNickname() {
		return kakaoAccount.profile.nickname;
	}

	@Override
	public OAuthProvider getOAuthProvider() {
		return OAuthProvider.KAKAO;
	}

	@Override
	public String getProfileImageUrl() {
		return kakaoAccount.profile.profileImageUrl;
	}

	@Override
	public String getGender() {
		return kakaoAccount.gender;
	}
}
