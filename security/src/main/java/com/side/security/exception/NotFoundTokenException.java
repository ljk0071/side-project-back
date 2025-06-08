package com.side.security.exception;

public class NotFoundTokenException extends IllegalArgumentException {

	public NotFoundTokenException(String message) {
		super(message);
	}
}
