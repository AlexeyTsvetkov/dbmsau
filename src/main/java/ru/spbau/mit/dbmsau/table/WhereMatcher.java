package ru.spbau.mit.dbmsau.table;

import ru.spbau.mit.dbmsau.pages.Record;

import java.util.List;

public interface WhereMatcher {
    public boolean matches(Record record);
}
