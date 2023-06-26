package com.side.common.exception;

public class NotAuthorizedException extends RuntimeException {

	public NotAuthorizedException() {
	}

	public NotAuthorizedException(Throwable throwable) {
		super(throwable);
	}

	public NotAuthorizedException(String message) {
		super(message);
	}

	public NotAuthorizedException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
