package com.side.security.exception;

public class InvalidTokenException extends IllegalArgumentException {

	public InvalidTokenException(String message) {
		super(message);
	}
}
