package com.seniors.common.exception.type;

import com.seniors.common.constant.ResultCode;

public class NotFoundException extends RuntimeException {
	private final ResultCode resultCode;

	public NotFoundException() {
		super(ResultCode.NOT_FOUND.getMessage());
		this.resultCode = ResultCode.NOT_FOUND;
	}

	public NotFoundException(String message) {
		super(ResultCode.NOT_FOUND.getMessage(message));
		this.resultCode = ResultCode.NOT_FOUND;
	}

	public NotFoundException(String message, Throwable cause) {
		super(ResultCode.NOT_FOUND.getMessage(message), cause);
		this.resultCode = ResultCode.NOT_FOUND;
	}

	public NotFoundException(Throwable cause) {
		super(ResultCode.NOT_FOUND.getMessage(cause));
		this.resultCode = ResultCode.NOT_FOUND;
	}

	public NotFoundException(ResultCode errorCode) {
		super(errorCode.getMessage());
		this.resultCode = errorCode;
	}

	public NotFoundException(ResultCode errorCode, String message) {
		super(errorCode.getMessage(message));
		this.resultCode = errorCode;
	}

	public NotFoundException(ResultCode errorCode, String message, Throwable cause) {
		super(errorCode.getMessage(message), cause);
		this.resultCode = errorCode;
	}

	public NotFoundException(ResultCode errorCode, Throwable cause) {
		super(errorCode.getMessage(cause), cause);
		this.resultCode = errorCode;
	}
}
