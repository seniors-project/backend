package com.seniors.domain.auth.kakao;

import com.seniors.common.constant.OAuthProvider;
import com.seniors.common.exception.type.NotAuthorizedException;
import com.seniors.domain.auth.common.OAuthApiClient;
import com.seniors.domain.auth.common.OAuthInfoResponse;
import com.seniors.domain.auth.common.OAuthLoginParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

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

	private final WebClient webClient;

	@Override
	public OAuthProvider oAuthProvider() {
		return OAuthProvider.KAKAO;
	}

	@Override
	public String requestAccessToken(OAuthLoginParams params) {
		String url = authUrl + "/oauth/token";

		MultiValueMap<String, String> body = params.makeBody();
		body.add("grant_type", GRANT_TYPE);
		body.add("client_id", clientId);

		return webClient.post()
				.uri(url)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.bodyValue(body)
				.retrieve()
				.bodyToMono(KakaoTokens.class)
				.switchIfEmpty(Mono.error(new NotAuthorizedException("Kakao Token Response Data is Null")))
				.map(KakaoTokens::getAccessToken)
				.block();
	}

	@Override
	public OAuthInfoResponse requestOAuthInfo(String accessToken) {
		String url = apiUrl + "/v2/user/me";

		return webClient.post()
				.uri(url)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.header("Authorization", "Bearer " + accessToken)
				.bodyValue("{\"property_keys\": [\"kakao_account.email\", \"kakao_account.profile\"]}")
				.retrieve()
				.bodyToMono(KakaoInfoResponse.class)
				.block();
	}
}

