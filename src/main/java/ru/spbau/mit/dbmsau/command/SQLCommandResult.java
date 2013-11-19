package ru.spbau.mit.dbmsau.command;

import java.util.Iterator;

public class SQLCommandResult implements Iterable<String> {
    private boolean ok = true;
    private Iterator<String> iteratorResult = null;

    public SQLCommandResult() {
    }

    public SQLCommandResult(Iterator<String> iterableResult) {
        this.iteratorResult = iterableResult;
    }

    public boolean isOk() {
        return ok;
    }

    public boolean isIterable() {
        return iteratorResult != null;
    }

    @Override
    public Iterator<String> iterator() {
        return iteratorResult;
    }
}
