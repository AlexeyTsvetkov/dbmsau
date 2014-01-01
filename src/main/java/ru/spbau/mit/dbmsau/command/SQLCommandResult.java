package ru.spbau.mit.dbmsau.command;

import java.util.Iterator;

public class SQLCommandResult implements Iterable<String> {
    private boolean ok = true;
    private Iterator<String> iteratorResult = null;
    private long executionTime = 0;
    private int rowsAffected = 0;

    public SQLCommandResult() {
    }

    public SQLCommandResult(Iterator<String> iterableResult) {
        this.iteratorResult = iterableResult;
    }

    public SQLCommandResult(int rowsAffected) {
        this.rowsAffected = rowsAffected;
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

    public long getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(long executionTime) {
        this.executionTime = executionTime;
    }

    public int getRowsAffected() {
        return rowsAffected;
    }
}
