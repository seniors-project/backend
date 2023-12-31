package com.seniors.domain.auth.common;

import com.seniors.common.constant.OAuthProvider;

public interface OAuthApiClient {
	OAuthProvider oAuthProvider();
	String requestAccessToken(OAuthLoginParams params);
	OAuthInfoResponse requestOAuthInfo(String accessToken);
}
