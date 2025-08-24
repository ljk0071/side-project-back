package com.side.security.exception;

import lombok.Getter;

@Getter
public class InvalidTokenException extends IllegalArgumentException {

    private final boolean needLogin;

    public InvalidTokenException(String message, boolean needLogin) {
        super(message);
        this.needLogin = needLogin;
    }
}
