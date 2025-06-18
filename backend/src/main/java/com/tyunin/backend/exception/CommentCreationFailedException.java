package com.tyunin.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CommentCreationFailedException extends RuntimeException {
	public CommentCreationFailedException() {
		super();
	}

	public CommentCreationFailedException(String msg) {
		super(msg);
	}
}
