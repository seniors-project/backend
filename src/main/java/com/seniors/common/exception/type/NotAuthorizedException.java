package com.seniors.common.exception.type;

import com.seniors.common.constant.ResultCode;

public class NotAuthorizedException extends RuntimeException {

	private final ResultCode resultCode;

	public NotAuthorizedException() {
		super(ResultCode.UNAUTHORIZED.getMessage());
		this.resultCode = ResultCode.UNAUTHORIZED;
	}

	public NotAuthorizedException(String message) {
		super(ResultCode.UNAUTHORIZED.getMessage(message));
		this.resultCode = ResultCode.UNAUTHORIZED;
	}

	public NotAuthorizedException(String message, Throwable cause) {
		super(ResultCode.UNAUTHORIZED.getMessage(message), cause);
		this.resultCode = ResultCode.UNAUTHORIZED;
	}

	public NotAuthorizedException(Throwable cause) {
		super(ResultCode.UNAUTHORIZED.getMessage(cause));
		this.resultCode = ResultCode.UNAUTHORIZED;
	}

	public NotAuthorizedException(ResultCode errorCode) {
		super(errorCode.getMessage());
		this.resultCode = errorCode;
	}

	public NotAuthorizedException(ResultCode errorCode, String message) {
		super(errorCode.getMessage(message));
		this.resultCode = errorCode;
	}

	public NotAuthorizedException(ResultCode errorCode, String message, Throwable cause) {
		super(errorCode.getMessage(message), cause);
		this.resultCode = errorCode;
	}

	public NotAuthorizedException(ResultCode errorCode, Throwable cause) {
		super(errorCode.getMessage(cause), cause);
		this.resultCode = errorCode;
	}
}
