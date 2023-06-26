package com.side.common.exception;

public class FileUploadException extends RuntimeException {

	public FileUploadException() {
	}

	public FileUploadException(Throwable throwable) {
		super(throwable);
	}

	public FileUploadException(String message) {
		super(message);
	}

	public FileUploadException(String message, Throwable throwable) {
		super(message, throwable);
	}
}