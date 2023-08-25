package com.seniors.common.exception.type;

import com.seniors.common.constant.ResultCode;

public class ForbiddenException extends RuntimeException{
	private final ResultCode resultCode;

	public ForbiddenException() {
		super(ResultCode.NOT_FOUND.getMessage());
		this.resultCode = ResultCode.NOT_FOUND;
	}

	public ForbiddenException(String message) {
		super(ResultCode.NOT_FOUND.getMessage(message));
		this.resultCode = ResultCode.NOT_FOUND;
	}

	public ForbiddenException(String message, Throwable cause) {
		super(ResultCode.NOT_FOUND.getMessage(message), cause);
		this.resultCode = ResultCode.NOT_FOUND;
	}

	public ForbiddenException(Throwable cause) {
		super(ResultCode.NOT_FOUND.getMessage(cause));
		this.resultCode = ResultCode.NOT_FOUND;
	}

	public ForbiddenException(ResultCode errorCode) {
		super(errorCode.getMessage());
		this.resultCode = errorCode;
	}

	public ForbiddenException(ResultCode errorCode, String message) {
		super(errorCode.getMessage(message));
		this.resultCode = errorCode;
	}

	public ForbiddenException(ResultCode errorCode, String message, Throwable cause) {
		super(errorCode.getMessage(message), cause);
		this.resultCode = errorCode;
	}

	public ForbiddenException(ResultCode errorCode, Throwable cause) {
		super(errorCode.getMessage(cause), cause);
		this.resultCode = errorCode;
	}
}
