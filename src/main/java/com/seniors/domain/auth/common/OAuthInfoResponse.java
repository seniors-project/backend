package com.seniors.domain.auth.common;

import com.seniors.common.constant.OAuthProvider;

public interface OAuthInfoResponse {
	String getSnsId();
	String getEmail();
	String getNickname();
	OAuthProvider getOAuthProvider();
}
