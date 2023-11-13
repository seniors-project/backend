package com.seniors.common.exception;

import com.seniors.common.constant.ResultCode;
import com.seniors.common.dto.ErrorResponse;
import com.seniors.common.exception.type.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class ExceptionRestControllerAdvice extends ResponseEntityExceptionHandler {

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<Object> handleBadRequestException(BadRequestException e, WebRequest request) {
		return handleExceptionInternal(e, ResultCode.BAD_REQUEST, request);
	}

	@ExceptionHandler(NotAuthorizedException.class)
	public ResponseEntity<Object> handleNotAuthorizedException(NotAuthorizedException e, WebRequest request) {
		return handleExceptionInternal(e, ResultCode.UNAUTHORIZED, request);
	}

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<Object> handleNotFoundException(NotFoundException e, WebRequest request) {
		return handleExceptionInternal(e, ResultCode.NOT_FOUND, request);
	}

	@ExceptionHandler(ConflictException.class)
	public ResponseEntity<Object> handleConflictException(ConflictException e, WebRequest request) {
		return handleExceptionInternal(e, ResultCode.CONFLICT, request);
	}

	@ExceptionHandler(ForbiddenException.class)
	public ResponseEntity<Object> handleForbiddenException(ForbiddenException e, WebRequest request) {
		return handleExceptionInternal(e, ResultCode.FORBIDDEN, request);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleAllException(Exception e, WebRequest request) {
		return handleExceptionInternal(e, ResultCode.INTERNAL_ERROR, request);
	}


	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
	                                                              HttpStatusCode statusCode, WebRequest request) {
		BindingResult bindingResult = ex.getBindingResult();
		List<ObjectError> objectErrors = bindingResult.getAllErrors();
		List<String> errorMessages = new ArrayList<>();
		for (ObjectError objectError : objectErrors) {
			errorMessages.add(objectError.getDefaultMessage());
		}
		String errorMessage = String.join(" ", errorMessages);
		ErrorResponse errorResponse = ErrorResponse.of(ResultCode.VALIDATION_ERROR, errorMessage);
		return new ResponseEntity<Object>(errorResponse, ResultCode.VALIDATION_ERROR.getHttpStatus());
	}


	private ResponseEntity<Object> handleExceptionInternal(Exception e, ResultCode errorCode,
	                                                       WebRequest request) {
		return super.handleExceptionInternal(
				e,
				ErrorResponse.of(errorCode, errorCode.getMessage(e)),
				HttpHeaders.EMPTY,
				errorCode.getHttpStatus(),
				request
		);
	}
}