package com.tynin.messanger.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ChatRoomCreationFailedException extends RuntimeException {
	public ChatRoomCreationFailedException() {
		super();
	}

	public ChatRoomCreationFailedException(String msg) {
		super(msg);
	}
}
