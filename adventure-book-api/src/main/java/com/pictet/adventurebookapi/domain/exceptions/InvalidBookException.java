package com.pictet.adventurebookapi.domain.exceptions;

public class InvalidBookException extends RuntimeException {
    public InvalidBookException(String message) {
        super(message);
    }
}
