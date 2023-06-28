package com.side.api.common.controllerAdvice;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomParameterHandler {
	/* ref: https://tech.kakaopay.com/post/martin-dev-honey-tip-1/
	* GetMethod의 QueryParam 공백 제거
	*/
	@InitBinder
	public void InitBinder(WebDataBinder dataBinder) {
		StringTrimmerEditor ste = new StringTrimmerEditor(true);
		dataBinder.registerCustomEditor(String.class, ste);
	}
}
