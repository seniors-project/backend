package com.seniors.common.exception.type;

import com.seniors.common.constant.ResultCode;
import lombok.Getter;

@Getter
public class InternalException extends RuntimeException {

	private final ResultCode resultCode;

	public InternalException() {
		super(ResultCode.INTERNAL_ERROR.getMessage());
		this.resultCode = ResultCode.INTERNAL_ERROR;
	}

	public InternalException(String message) {
		super(ResultCode.INTERNAL_ERROR.getMessage(message));
		this.resultCode = ResultCode.INTERNAL_ERROR;
	}

	public InternalException(String message, Throwable cause) {
		super(ResultCode.INTERNAL_ERROR.getMessage(message), cause);
		this.resultCode = ResultCode.INTERNAL_ERROR;
	}

	public InternalException(Throwable cause) {
		super(ResultCode.INTERNAL_ERROR.getMessage(cause));
		this.resultCode = ResultCode.INTERNAL_ERROR;
	}

	public InternalException(ResultCode errorCode) {
		super(errorCode.getMessage());
		this.resultCode = errorCode;
	}

	public InternalException(ResultCode errorCode, String message) {
		super(errorCode.getMessage(message));
		this.resultCode = errorCode;
	}

	public InternalException(ResultCode errorCode, String message, Throwable cause) {
		super(errorCode.getMessage(message), cause);
		this.resultCode = errorCode;
	}

	public InternalException(ResultCode errorCode, Throwable cause) {
		super(errorCode.getMessage(cause), cause);
		this.resultCode = errorCode;
	}
}
