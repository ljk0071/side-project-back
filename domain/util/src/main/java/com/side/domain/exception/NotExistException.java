package com.side.domain.exception;

import lombok.Getter;

@Getter
public class NotExistException extends RuntimeException {

    private final long id;

    public NotExistException(String message, long id) {
        super(message);
        this.id = id;
    }
}
