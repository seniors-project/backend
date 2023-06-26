package com.side.common.exception;

public class JsonException extends RuntimeException{
	public JsonException() {
	}

	public JsonException(Throwable throwable) {
		super(throwable);
	}

	public JsonException(String message) {
		super(message);
	}

	public JsonException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
