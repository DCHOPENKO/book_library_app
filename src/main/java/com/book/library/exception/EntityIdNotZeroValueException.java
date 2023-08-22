package com.book.library.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus
public class EntityIdNotZeroValueException extends RuntimeException {
    public EntityIdNotZeroValueException(String message) {
        super(message);
    }

    public EntityIdNotZeroValueException(String message, Throwable cause) {
        super(message, cause);
    }
}
