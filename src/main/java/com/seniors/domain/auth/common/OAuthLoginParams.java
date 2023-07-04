package com.seniors.domain.auth.common;

import com.seniors.common.constant.OAuthProvider;
import org.springframework.util.MultiValueMap;

public interface OAuthLoginParams {
	OAuthProvider oAuthProvider();
	MultiValueMap<String, String> makeBody();
}
