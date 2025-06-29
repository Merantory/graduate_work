package com.tyunin.sso.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserAlreadyExistException extends RuntimeException {
	public UserAlreadyExistException() {
		super();
	}

	public UserAlreadyExistException(String msg) {
		super(msg);
	}
}
