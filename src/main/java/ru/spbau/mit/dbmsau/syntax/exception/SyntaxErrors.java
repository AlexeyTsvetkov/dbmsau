package ru.spbau.mit.dbmsau.syntax.exception;


import ru.spbau.mit.dbmsau.exception.UserError;

import java.util.LinkedList;
import java.util.List;

public class SyntaxErrors extends UserError {
    private List< String > errors;

    public SyntaxErrors(List<String> errors) {
        this.errors = errors;
    }

    public SyntaxErrors(String s) {
        errors = new LinkedList<>();
        errors.add(s);
    }

    @Override
    public String getMessage() {
        StringBuilder result = new StringBuilder();
        for (String error : errors) {
            result.append(error);
            result.append("\n");
        }

        return result.toString();
    }
}
