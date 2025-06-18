package com.tynin.messanger.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MessageCreationFailedException extends RuntimeException {
	public MessageCreationFailedException() {
		super();
	}

	public MessageCreationFailedException(String msg) {
		super(msg);
	}
}
