package com.side.common.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum ResultCode {
	NONE("NONE"),
	EXISTS_NICKNAME("존재하는 닉네임"),
	EXISTS_EMAIL("존재하는 이메일"),
	NOT_EXISTS_EMAIL("존재하지 않는 이메일"),
	EXPIRED_REFRESH_TOKEN("만료된 리프레시 토큰"),
	NOT_REQUIRED_SIGN_UP_VERIFICATION("회원가입 인증이 필요하지 않음"),

	NOT_EXISTS_REPORT("존재하지 않는 문의"),

	INVALID_USER_STATUS("유효하지 않은 사용자 상태"),
	;

	private final String desc;

	@JsonValue
	public String getValue() {
		return this.name();
	}

	@JsonCreator
	public static ResultCode of(String name) {
		for (ResultCode obj : ResultCode.values()) {
			if (name.equalsIgnoreCase(obj.name())) {
				return obj;
			}
		}

		return ResultCode.NONE;
	}

	public static List<Map<String, Object>> codes() {
		return Arrays.stream(ResultCode.values()).map(o -> {
			Map<String, Object> map = new HashMap<>();
			map.put("name", o.name());
			map.put("desc", o.getDesc());
			return map;
		}).collect(Collectors.toList());
	}
}