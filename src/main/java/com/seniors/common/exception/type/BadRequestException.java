package com.seniors.common.exception.type;

import com.seniors.common.constant.ResultCode;

public class BadRequestException extends RuntimeException{

	private final ResultCode resultCode;

	public BadRequestException() {
		super(ResultCode.BAD_REQUEST.getMessage());
		this.resultCode = ResultCode.BAD_REQUEST;
	}

	public BadRequestException(String message) {
		super(ResultCode.BAD_REQUEST.getMessage(message));
		this.resultCode = ResultCode.BAD_REQUEST;
	}

	public BadRequestException(String message, Throwable cause) {
		super(ResultCode.BAD_REQUEST.getMessage(message), cause);
		this.resultCode = ResultCode.BAD_REQUEST;
	}

	public BadRequestException(Throwable cause) {
		super(ResultCode.BAD_REQUEST.getMessage(cause));
		this.resultCode = ResultCode.BAD_REQUEST;
	}

	public BadRequestException(ResultCode errorCode) {
		super(errorCode.getMessage());
		this.resultCode = errorCode;
	}

	public BadRequestException(ResultCode errorCode, String message) {
		super(errorCode.getMessage(message));
		this.resultCode = errorCode;
	}

	public BadRequestException(ResultCode errorCode, String message, Throwable cause) {
		super(errorCode.getMessage(message), cause);
		this.resultCode = errorCode;
	}

	public BadRequestException(ResultCode errorCode, Throwable cause) {
		super(errorCode.getMessage(cause), cause);
		this.resultCode = errorCode;
	}
}
