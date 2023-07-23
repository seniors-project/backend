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
	OK(200, HttpStatus.OK, "Ok"),

	// 400대 에러
	BAD_REQUEST(400, HttpStatus.BAD_REQUEST, "Bad request"),
	VALIDATION_ERROR(400, HttpStatus.BAD_REQUEST, "Validation error"),
	NOT_FOUND(404, HttpStatus.NOT_FOUND, "Requested resource is not found"),

	// 500대 에러
	INTERNAL_ERROR(500, HttpStatus.INTERNAL_SERVER_ERROR, "Internal error"),

	// 인증
	UNAUTHORIZED(401, HttpStatus.UNAUTHORIZED, "User unauthorized"),
	EXPIRED_REFRESH_TOKEN(403, HttpStatus.FORBIDDEN, "Expired Refresh-token");

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