package ru.spbau.mit.dbmsau.syntax.exception;


import java.util.LinkedList;
import java.util.List;

public class SyntaxErrorsException extends Exception {
    private List< String > errors;

    public SyntaxErrorsException(List<String> errors) {
        this.errors = errors;
    }

    public SyntaxErrorsException(String s) {
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
