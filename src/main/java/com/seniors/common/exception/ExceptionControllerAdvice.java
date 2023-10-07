package com.seniors.common.exception;

import com.seniors.common.dto.ResponseDto;
import com.seniors.common.exception.type.ViewException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

import static com.seniors.common.constant.ResultCode.BAD_REQUEST;

@Slf4j
@ControllerAdvice
public class ExceptionControllerAdvice {

	@ExceptionHandler(ViewException.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public ModelAndView viewException(ViewException e) {
		log.error("Unknown Page Error.", e);
		ModelAndView mv = new ModelAndView("error/500");
		mv.addObject("errorMessage", e.getMessage());
		return mv;
	}

}
