package com.seniors.common.exception.type;

public class ConflictException extends RuntimeException {

	public ConflictException() {
	}

	public ConflictException(Throwable throwable) {
		super(throwable);
	}

	public ConflictException(String message) {
		super(message);
	}

	public ConflictException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
