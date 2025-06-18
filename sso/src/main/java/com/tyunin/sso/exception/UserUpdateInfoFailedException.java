package com.tyunin.sso.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserUpdateInfoFailedException extends RuntimeException {
	public UserUpdateInfoFailedException() {
		super();
	}

	public UserUpdateInfoFailedException(String msg) {
		super(msg);
	}
}
