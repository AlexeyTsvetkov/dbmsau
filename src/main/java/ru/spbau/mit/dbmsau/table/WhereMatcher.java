package ru.spbau.mit.dbmsau.table;

import ru.spbau.mit.dbmsau.pages.Record;

public interface WhereMatcher {
    public boolean matches(Record record);
}
