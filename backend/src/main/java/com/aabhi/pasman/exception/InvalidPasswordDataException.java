package com.aabhi.pasman.exception;

public class InvalidPasswordDataException extends RuntimeException {
    public InvalidPasswordDataException(String message) {
        super(message);
    }
}