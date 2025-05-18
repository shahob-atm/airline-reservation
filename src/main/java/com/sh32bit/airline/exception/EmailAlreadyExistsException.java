package com.sh32bit.airline.exception;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException() {
        super("Email is already registered");
    }
}
