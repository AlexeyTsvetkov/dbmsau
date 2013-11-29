package ru.spbau.mit.dbmsau.table.exception;

import ru.spbau.mit.dbmsau.exception.UserError;

public class SemanticError extends UserError {
    public SemanticError(String message) {
        super("Semantic Error: " + message);
    }
}
