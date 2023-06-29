package com.seniors.common.exception.post;

import com.seniors.common.exception.CustomException;

public class PostNotFound extends CustomException {
	public PostNotFound() {
	}

	public PostNotFound(Throwable throwable) {
		super(throwable);
	}

	public PostNotFound(String message) {
		super(message);
	}

	public PostNotFound(String message, Throwable throwable) {
		super(message, throwable);
	}
}
