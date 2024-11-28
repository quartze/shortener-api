package com.quartze.shortenerurl.exceptions;

public class BodyIsRequiredException extends RuntimeException {
    public BodyIsRequiredException(String message) {
        super(message);
    }
}
