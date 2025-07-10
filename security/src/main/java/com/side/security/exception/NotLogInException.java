package com.side.security.exception;

public class NotLogInException extends RuntimeException {
    public NotLogInException(String message) {
        super(message);
    }
}
