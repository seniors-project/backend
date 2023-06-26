package com.side.common.exception;

public class ViewException extends RuntimeException{

	public ViewException() {
	}

	public ViewException(Throwable throwable) {
		super(throwable);
	}

	public ViewException(String message) {
		super(message);
	}

	public ViewException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
