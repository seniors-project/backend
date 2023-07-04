package com.seniors.common.exception.type;

import com.seniors.common.constant.ResultCode;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

	private final ResultCode resultCode;

	public CustomException() {
		super(ResultCode.INTERNAL_ERROR.getMessage());
		this.resultCode = ResultCode.INTERNAL_ERROR;
	}

	public CustomException(String message) {
		super(ResultCode.INTERNAL_ERROR.getMessage(message));
		this.resultCode = ResultCode.INTERNAL_ERROR;
	}

	public CustomException(String message, Throwable cause) {
		super(ResultCode.INTERNAL_ERROR.getMessage(message), cause);
		this.resultCode = ResultCode.INTERNAL_ERROR;
	}

	public CustomException(Throwable cause) {
		super(ResultCode.INTERNAL_ERROR.getMessage(cause));
		this.resultCode = ResultCode.INTERNAL_ERROR;
	}

	public CustomException(ResultCode errorCode) {
		super(errorCode.getMessage());
		this.resultCode = errorCode;
	}

	public CustomException(ResultCode errorCode, String message) {
		super(errorCode.getMessage(message));
		this.resultCode = errorCode;
	}

	public CustomException(ResultCode errorCode, String message, Throwable cause) {
		super(errorCode.getMessage(message), cause);
		this.resultCode = errorCode;
	}

	public CustomException(ResultCode errorCode, Throwable cause) {
		super(errorCode.getMessage(cause), cause);
		this.resultCode = errorCode;
	}
}
