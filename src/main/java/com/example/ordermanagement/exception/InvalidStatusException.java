package com.example.ordermanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// thrown when trying to do an illegal status transition
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidStatusException extends RuntimeException {
    public InvalidStatusException(String message) {
        super(message);
    }
}
