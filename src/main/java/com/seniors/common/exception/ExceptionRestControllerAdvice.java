package com.seniors.common.exception;

import com.seniors.common.constant.ResultCode;
import com.seniors.common.dto.ErrorResponse;
import com.seniors.common.dto.ResponseDto;
import com.seniors.common.exception.type.BadRequestException;
import com.seniors.common.exception.type.InternalException;
import com.seniors.common.exception.type.NotFoundException;
import com.seniors.common.exception.type.ViewException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

import static com.seniors.common.constant.ResultCode.BAD_REQUEST;

@Slf4j
@RestControllerAdvice
public class ExceptionRestControllerAdvice extends ResponseEntityExceptionHandler {
	@ExceptionHandler
	public ResponseEntity<Object> validation(ConstraintViolationException e, WebRequest request) {
		return handleExceptionInternal(e, ResultCode.VALIDATION_ERROR, request);
	}

	@ExceptionHandler
	public ResponseEntity<Object> general(InternalException e, WebRequest request) {
		return handleExceptionInternal(e, e.getResultCode(), request);
	}

	@ExceptionHandler
	public ResponseEntity<Object> exception(Exception e, WebRequest request) {
		return handleExceptionInternal(e, ResultCode.INTERNAL_ERROR, request);
	}

	@ExceptionHandler
	public ResponseEntity<Object> handleBadRequestException(BadRequestException e, WebRequest request) {
		return handleExceptionInternal(e, ResultCode.BAD_REQUEST, request);
	}

	@ExceptionHandler
	public ResponseEntity<Object> handleNotFoundException(NotFoundException e, WebRequest request) {
		return handleExceptionInternal(e, ResultCode.NOT_FOUND, request);
	}

	@ExceptionHandler(ViewException.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public ModelAndView viewException(ViewException e) {
		log.error("Unknown Page Error.", e);
		ModelAndView mv = new ModelAndView("error/500");
		mv.addObject("errorMessage", e.getMessage());
		return mv;
	}

	@ExceptionHandler(BindException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseDto validation(BindException e) {
		BindingResult bindingResult = e.getBindingResult();
		List<ObjectError> objectErrors = bindingResult.getAllErrors();
		List<String> errorMessages = new ArrayList<>();

		for (ObjectError objectError : objectErrors) {
			errorMessages.add(objectError.getDefaultMessage());
		}
		String errorMessage = String.join(", ", errorMessages);
		return ResponseDto.of(false, BAD_REQUEST, errorMessage);
	}


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
