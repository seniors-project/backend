package com.seniors.common.exception.type;

import com.seniors.common.constant.ResultCode;

public class BadRequestException extends RuntimeException{

	private final ResultCode resultCode;

	public BadRequestException() {
		super(ResultCode.INTERNAL_ERROR.getMessage());
		this.resultCode = ResultCode.INTERNAL_ERROR;
	}

	public BadRequestException(String message) {
		super(ResultCode.INTERNAL_ERROR.getMessage(message));
		this.resultCode = ResultCode.INTERNAL_ERROR;
	}

	public BadRequestException(String message, Throwable cause) {
		super(ResultCode.INTERNAL_ERROR.getMessage(message), cause);
		this.resultCode = ResultCode.INTERNAL_ERROR;
	}

	public BadRequestException(Throwable cause) {
		super(ResultCode.INTERNAL_ERROR.getMessage(cause));
		this.resultCode = ResultCode.INTERNAL_ERROR;
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
