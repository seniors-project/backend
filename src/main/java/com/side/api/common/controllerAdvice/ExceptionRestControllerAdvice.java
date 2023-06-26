package com.side.api.common.controllerAdvice;

import com.side.api.common.dto.ErrorResponse;
import com.side.common.exception.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import java.nio.file.AccessDeniedException;

@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class ExceptionRestControllerAdvice {
	@ExceptionHandler({Exception.class})
	public ResponseEntity<ErrorResponse> exception(Exception e) {
		String errorMessage = "Unknown Error.";
		log.error(errorMessage, e);
		ErrorResponse errorResponse = ErrorResponse.builder().message(errorMessage).build();
		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler({HttpClientErrorException.class,
			MissingServletRequestParameterException.class})
	public ResponseEntity<ErrorResponse> badRequest(Exception e) {
		log.warn("Bad Request.", e.getMessage());
		ErrorResponse errorResponse = ErrorResponse.builder().message(e.getMessage()).build();
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({IllegalArgumentException.class})
	public ResponseEntity<ErrorResponse> messageNotReadable(Exception e) {
		log.warn("Bad Request.", e.getMessage());
		ErrorResponse errorResponse = ErrorResponse.builder().message(e.getMessage()).build();
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({HttpMessageNotReadableException.class})
	public ResponseEntity<ErrorResponse> messageNotReadable(HttpMessageNotReadableException e) {
		log.warn("Bad Request.", e.getMessage());
		ErrorResponse errorResponse = ErrorResponse.builder()
				.message(e.getMostSpecificCause().getMessage()).build();
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({MethodArgumentNotValidException.class})
	public ResponseEntity<ErrorResponse> methodArgumentNotValidation(
			MethodArgumentNotValidException e) {
		String errorMessage =
				String.format("[%s] : %s", e.getBindingResult().getFieldError().getField(),
						e.getBindingResult().getFieldError().getDefaultMessage());
		ErrorResponse errorResponse = ErrorResponse.builder().message(errorMessage).build();
		log.warn("Bad Request.", errorMessage);
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({EntityNotFoundException.class, EmptyResultDataAccessException.class})
	public ResponseEntity<ErrorResponse> entityNotFound(Exception e) {
		log.warn("Not Found.", e.getMessage());
		ErrorResponse errorResponse = ErrorResponse.builder().message(e.getMessage()).build();
		return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler({FileUploadException.class})
	public ResponseEntity<ErrorResponse> fileUploadException(FileUploadException e) {
		String errorMessage = "FileUploadException.";
		log.error(errorMessage, e);
		ErrorResponse errorResponse = ErrorResponse.builder().message(errorMessage).build();
		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<?> customException(CustomException e) {
		String logMessage = "Bad Request.";
		HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

		log.warn(logMessage, e.getMessage());

		ErrorResponse errorResponse = ErrorResponse.builder().message(e.getMessage())
				.resultCode(e.getResultCode())
				.message(e.getResultCode().getDesc())
				.build();
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({FailEncryptException.class})
	public ResponseEntity<ErrorResponse> failEncrypt(Exception e) {
		log.error("Fail Encrypt.", e);
		ErrorResponse errorResponse = ErrorResponse.builder().message(e.getMessage()).build();
		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler({AccessDeniedException.class})
	public ResponseEntity<ErrorResponse> forbidden(Exception e) {
		log.warn("Forbidden.", e.getMessage());
		ErrorResponse errorResponse = ErrorResponse.builder().message(e.getMessage()).build();
		return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler({NotAuthorizedException.class})
	public ResponseEntity<ErrorResponse> unAuthorized(Exception e) {
		log.warn("UnAuthorized.", e.getMessage());
		ErrorResponse errorResponse = ErrorResponse.builder().message(e.getMessage()).build();
		return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler({BindException.class})
	public ResponseEntity<ErrorResponse> validationBindException(BindException e) {
		log.warn("Bad Request.", e.getMessage());
		String errorMessage =
				String.format("[%s] : %s", e.getBindingResult().getFieldError().getField(),
						e.getBindingResult().getFieldError().getDefaultMessage());
		ErrorResponse errorResponse = ErrorResponse.builder().message(errorMessage).build();
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({ConflictException.class})
	public ResponseEntity<ErrorResponse> conflict(Exception e) {
		log.warn("Conflict.", e.getMessage());
		ErrorResponse errorResponse = ErrorResponse.builder().message(e.getMessage()).build();
		return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
	}
}
