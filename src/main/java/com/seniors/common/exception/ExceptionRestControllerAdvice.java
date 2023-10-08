package com.seniors.common.exception;

import com.seniors.common.constant.ResultCode;
import com.seniors.common.dto.ErrorResponse;
import com.seniors.common.dto.ResponseDto;
import com.seniors.common.exception.type.BadRequestException;
import com.seniors.common.exception.type.NotFoundException;
import com.seniors.common.exception.type.ViewException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> handleBadRequestException(BadRequestException e, WebRequest request) {
        return handleExceptionInternal(e, ResultCode.BAD_REQUEST, request);
    }

    @ExceptionHandler(NotFoundException.class)
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