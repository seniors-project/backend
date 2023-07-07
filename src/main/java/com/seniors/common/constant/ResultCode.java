package com.seniors.common.constant;

import com.seniors.common.exception.type.InternalException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

@Getter
@RequiredArgsConstructor
public enum ResultCode {

	// Common Response
	OK(0, HttpStatus.OK, "Ok"),

	// 400대 에러
	BAD_REQUEST(10000, HttpStatus.BAD_REQUEST, "Bad request"),
	VALIDATION_ERROR(10001, HttpStatus.BAD_REQUEST, "Validation error"),
	NOT_FOUND(10002, HttpStatus.NOT_FOUND, "Requested resource is not found"),

	// 500대 에러
	INTERNAL_ERROR(20000, HttpStatus.INTERNAL_SERVER_ERROR, "Internal error"),
	DATA_ACCESS_ERROR(20001, HttpStatus.INTERNAL_SERVER_ERROR, "Data access error"),

	// 인증
	UNAUTHORIZED(40000, HttpStatus.UNAUTHORIZED, "User unauthorized");

	private final Integer code;
	private final HttpStatus httpStatus;
	private final String message;

	public String getMessage(Throwable e) {
		return this.getMessage(this.getMessage() + " - " + e.getMessage());
		// 결과 예시 - "Validation error - Reason why it isn't valid"
	}

	public String getMessage(String message) {
		return Optional.ofNullable(message)
				.filter(Predicate.not(String::isBlank))
				.orElse(this.getMessage());
	}

	public static ResultCode valueOf(HttpStatus httpStatus) {
		if (httpStatus == null) {
			throw new InternalException("HttpStatus is null.");
		}

		return Arrays.stream(values())
				.filter(errorCode -> errorCode.getHttpStatus() == httpStatus)
				.findFirst()
				.orElseGet(() -> {
					if (httpStatus.is4xxClientError()) {
						return ResultCode.BAD_REQUEST;
					} else if (httpStatus.is5xxServerError()) {
						return ResultCode.INTERNAL_ERROR;
					} else {
						return ResultCode.OK;
					}
				});
	}

	@Override
	public String toString() {
		return String.format("%s (%d)", this.name(), this.getCode());
	}
}