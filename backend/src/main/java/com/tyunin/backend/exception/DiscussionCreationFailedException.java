package com.tyunin.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DiscussionCreationFailedException extends RuntimeException {
	public DiscussionCreationFailedException() {
		super();
	}

	public DiscussionCreationFailedException(String msg) {
		super(msg);
	}
}
