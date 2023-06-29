package com.seniors.api.common.dto;

import com.seniors.common.constant.ResultCode;

public class ErrorResponse extends ResponseDto {

	private ErrorResponse(ResultCode errorCode) {
		super(false, errorCode.getCode(), errorCode.getMessage());
	}

	private ErrorResponse(ResultCode errorCode, Exception e) {
		super(false, errorCode.getCode(), errorCode.getMessage(e));
	}

	private ErrorResponse(ResultCode errorCode, String message) {
		super(false, errorCode.getCode(), errorCode.getMessage(message));
	}

	public static ErrorResponse of(ResultCode errorCode) {
		return new ErrorResponse(errorCode);
	}

	public static ErrorResponse of(ResultCode errorCode, Exception e) {
		return new ErrorResponse(errorCode, e);
	}

	public static ErrorResponse of(ResultCode errorCode, String message) {
		return new ErrorResponse(errorCode, message);
	}
}