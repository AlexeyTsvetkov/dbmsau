package ru.spbau.mit.dbmsau.index.exception;

import ru.spbau.mit.dbmsau.exception.UserError;

public class IndexCreationError extends UserError {
    public IndexCreationError(String message) {
        super(message);
    }
}
