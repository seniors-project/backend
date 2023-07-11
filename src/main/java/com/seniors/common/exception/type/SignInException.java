package com.seniors.common.exception.type;

import com.seniors.common.constant.ResultCode;

public class SignInException extends Exception{
	private final ResultCode resultCode;

	public SignInException() {
		super(ResultCode.UNAUTHORIZED.getMessage());
		this.resultCode = ResultCode.UNAUTHORIZED;
	}

	public SignInException(String message) {
		super(ResultCode.UNAUTHORIZED.getMessage(message));
		this.resultCode = ResultCode.UNAUTHORIZED;
	}

	public SignInException(String message, Throwable cause) {
		super(ResultCode.UNAUTHORIZED.getMessage(message), cause);
		this.resultCode = ResultCode.UNAUTHORIZED;
	}

	public SignInException(Throwable cause) {
		super(ResultCode.UNAUTHORIZED.getMessage(cause));
		this.resultCode = ResultCode.UNAUTHORIZED;
	}

	public SignInException(ResultCode errorCode) {
		super(errorCode.getMessage());
		this.resultCode = errorCode;
	}

	public SignInException(ResultCode errorCode, String message) {
		super(errorCode.getMessage(message));
		this.resultCode = errorCode;
	}

	public SignInException(ResultCode errorCode, String message, Throwable cause) {
		super(errorCode.getMessage(message), cause);
		this.resultCode = errorCode;
	}

	public SignInException(ResultCode errorCode, Throwable cause) {
		super(errorCode.getMessage(cause), cause);
		this.resultCode = errorCode;
	}
}
