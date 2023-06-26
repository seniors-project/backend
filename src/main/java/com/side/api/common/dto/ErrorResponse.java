package com.side.api.common.dto;

import com.side.common.constant.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

	@Builder.Default
	private ResultCode resultCode = ResultCode.NONE;
	private String message;
	private Map<String, Object> data;
}