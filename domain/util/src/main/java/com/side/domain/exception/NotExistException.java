package com.side.domain.exception;

import lombok.Getter;

@Getter
public class NotExistException extends RuntimeException {

    private long id;

    public NotExistException(String message) {
        super(message);
    }

    public NotExistException(String message, long id) {
        super(message);
        this.id = id;
    }
}
