package ru.spbau.mit.dbmsau.index.exception;

public class DuplicateKeyException extends Exception {
    public DuplicateKeyException(String message) {
        super(message);
    }
}
