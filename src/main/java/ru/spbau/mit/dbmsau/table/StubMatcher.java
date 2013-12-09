package ru.spbau.mit.dbmsau.table;

public class StubMatcher implements WhereMatcher {
    @Override
    public boolean matches(TableRecord record) {
        return true;
    }
}
