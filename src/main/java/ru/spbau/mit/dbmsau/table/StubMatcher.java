package ru.spbau.mit.dbmsau.table;

import ru.spbau.mit.dbmsau.pages.Record;

public class StubMatcher implements WhereMatcher {
    @Override
    public boolean matches(Record record) {
        return true;
    }
}
