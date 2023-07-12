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

	@JsonIgnoreProperties(ignoreUnknown = true)
	private String id;

	@Getter
	@JsonIgnoreProperties(ignoreUnknown = true)
	static class KakaoAccount {
		private KakaoProfile profile;
		private String id;
		private String email;
		private String gender;
		private String birthday;
		private String age_range;
	}

	@Getter
	@JsonIgnoreProperties(ignoreUnknown = true)
	static class KakaoProfile {
		private String id;
		private String nickname;
		private String profile_image;
	}

	@Override
	public String getSnsId() {
		return id;
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
		return kakaoAccount.profile.profile_image;
	}

	@Override
	public String getGender() {
		return kakaoAccount.gender;
	}

	@Override
	public String getBirthday() {
		String month = kakaoAccount.birthday.substring(0, 2);
		String day = kakaoAccount.birthday.substring(2);
		return month + "-" + day;
	}

	@Override
	public String getAgeRange() {
		return kakaoAccount.age_range;
	}


}
