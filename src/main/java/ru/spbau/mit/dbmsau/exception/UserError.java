package ru.spbau.mit.dbmsau.exception;

public class UserError extends RuntimeException {
    public UserError() {
    }

    public UserError(String message) {
        super(message);
    }
}
