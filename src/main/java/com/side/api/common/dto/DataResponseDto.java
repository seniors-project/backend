package com.side.api.common.dto;

import com.side.common.constant.ResultCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class DataResponseDto<T> extends ResponseDto {

	private final Object data;

	private DataResponseDto(Object data) {
		super(true, ResultCode.OK.getCode(), ResultCode.OK.getMessage());
		this.data = data;
	}

	private DataResponseDto(Object data, String message) {
		super(true, ResultCode.OK.getCode(), message);
		this.data = data;
	}

	public static <T> DataResponseDto<T> of(T data) {
		if (data instanceof String) {
			return new DataResponseDto<>(new MessageResponse((String) data));
		} else {
			return new DataResponseDto<>(data);
		}
	}

	public static <T> DataResponseDto<T> of(T data, String message) {
		return new DataResponseDto<>(data, message);
	}

	public static <T> DataResponseDto<T> empty() {
		return new DataResponseDto<>(null);
	}

	@Getter
	@Slf4j
	private static class MessageResponse {

		private final String message;

		private MessageResponse(String message) {
			this.message = message;
		}
	}
}
