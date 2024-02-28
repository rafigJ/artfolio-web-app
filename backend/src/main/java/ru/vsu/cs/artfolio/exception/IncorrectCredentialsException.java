package ru.vsu.cs.artfolio.exception;

import org.springframework.http.HttpStatus;

public class IncorrectCredentialsException extends RestException {
    public IncorrectCredentialsException() {
        super("Incorrect credentials", HttpStatus.BAD_REQUEST);
    }
}
