package com.seniors.domain.auth.kakao;

import com.seniors.domain.auth.common.OAuthLoginParams;
import com.seniors.common.constant.OAuthProvider;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Getter
@NoArgsConstructor
public class KakaoLoginParams implements OAuthLoginParams {
	private String authorizationCode;

	@Override
	public OAuthProvider oAuthProvider() {
		return OAuthProvider.KAKAO;
	}

	// Kakao Server에서 response받은 code 값 chaining
	@Override
	public MultiValueMap<String, String> makeBody() {
		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("code", authorizationCode);
		return body;
	}
}
