package com.seniors.domain.auth.kakao;

import com.seniors.common.exception.type.NotAuthorizedException;
import com.seniors.domain.auth.common.OAuthApiClient;
import com.seniors.domain.auth.common.OAuthInfoResponse;
import com.seniors.domain.auth.common.OAuthLoginParams;
import com.seniors.common.constant.OAuthProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class KakaoApiClient implements OAuthApiClient {
	private static final String GRANT_TYPE = "authorization_code";

	@Value("${oauth.kakao.url.auth}")
	private String authUrl;

	@Value("${oauth.kakao.url.api}")
	private String apiUrl;

	@Value("${oauth.kakao.client-id}")
	private String clientId;

	private final RestTemplate restTemplate;

	@Override
	public OAuthProvider oAuthProvider() {
		return OAuthProvider.KAKAO;
	}

	@Override
	public String requestAccessToken(OAuthLoginParams params) {
		String url = authUrl + "/oauth/token";

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> body = params.makeBody();
		body.add("grant_type", GRANT_TYPE);
		body.add("client_id", clientId);

		HttpEntity<?> request = new HttpEntity<>(body, httpHeaders);

		KakaoTokens response = restTemplate.postForObject(url, request, KakaoTokens.class);

		if (response == null) {
			throw new NotAuthorizedException("Kakao Token Response Data is Null");
		}
		return response.getAccessToken();
	}

	@Override
	public OAuthInfoResponse requestOAuthInfo(String accessToken) {
		String url = apiUrl + "/v2/user/me";

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		httpHeaders.set("Authorization", "Bearer " + accessToken);

		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("property_keys", "[\"id\", \"kakao_account.\", \"properties.\", \"has_signed_up.\"]");

		HttpEntity<?> request = new HttpEntity<>(body, httpHeaders);

		return restTemplate.postForObject(url, request, KakaoInfoResponse.class);
	}
}
