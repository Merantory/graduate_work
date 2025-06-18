package com.tyunin.sso.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserUpdateRoleFailedException extends RuntimeException {
	public UserUpdateRoleFailedException() {
		super();
	}

	public UserUpdateRoleFailedException(String msg) {
		super(msg);
	}
}
