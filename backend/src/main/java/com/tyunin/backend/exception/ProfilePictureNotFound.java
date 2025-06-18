package com.tyunin.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProfilePictureNotFound extends RuntimeException {

	public ProfilePictureNotFound() { super(); }

	public ProfilePictureNotFound(String msg) { super(msg); }

}
