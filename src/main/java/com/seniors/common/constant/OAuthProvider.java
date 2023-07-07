package com.seniors.common.constant;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OAuthProvider {
	KAKAO("KAKAO");
	private final String desc;

	@JsonValue
	public String getValue() {
		return this.name();
	}
}
