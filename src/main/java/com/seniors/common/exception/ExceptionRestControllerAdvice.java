package com.seniors.common.exception;

import com.seniors.common.dto.ErrorResponse;
import com.seniors.common.constant.ResultCode;
import com.seniors.common.exception.type.CustomException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class ExceptionRestControllerAdvice extends ResponseEntityExceptionHandler {
	@ExceptionHandler
	public ResponseEntity<Object> validation(ConstraintViolationException e, WebRequest request) {
		return handleExceptionInternal(e, ResultCode.VALIDATION_ERROR, request);
	}

	@ExceptionHandler
	public ResponseEntity<Object> general(CustomException e, WebRequest request) {
		return handleExceptionInternal(e, e.getResultCode(), request);
	}

	@ExceptionHandler
	public ResponseEntity<Object> exception(Exception e, WebRequest request) {
		return handleExceptionInternal(e, ResultCode.INTERNAL_ERROR, request);
	}

//	@Override
//	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body,
//	                                                         HttpHeaders headers, HttpStatus status, WebRequest request) {
//		return handleExceptionInternal(ex,  ResultCode.valueOf(status), headers, status, request);
//	}


	private ResponseEntity<Object> handleExceptionInternal(Exception e, ResultCode errorCode,
	                                                       WebRequest request) {
		return handleExceptionInternal(e, errorCode, HttpHeaders.EMPTY, errorCode.getHttpStatus(),
				request);
	}

	private ResponseEntity<Object> handleExceptionInternal(Exception e, ResultCode errorCode,
	                                                       HttpHeaders headers, HttpStatus status, WebRequest request) {
		return super.handleExceptionInternal(
				e,
				ErrorResponse.of(errorCode, errorCode.getMessage(e)),
				headers,
				status,
				request
		);
	}
}
