package ru.spbau.mit.dbmsau.table;

public interface WhereMatcher {
    public boolean matches(TableRecord record);
}
