package ru.spbau.mit.dbmsau.command;

import ru.spbau.mit.dbmsau.pages.Record;
import ru.spbau.mit.dbmsau.table.ClauseIterator;
import ru.spbau.mit.dbmsau.table.RecordComparisonClause;
import ru.spbau.mit.dbmsau.table.WhereMatcher;

import java.util.List;

public class TableRecordMatcher implements WhereMatcher {

    private ClauseIterator clauses;

    TableRecordMatcher(ClauseIterator clauses) {
        this.clauses = clauses;
    }

    @Override
    public boolean matches(Record record) {
        if(clauses.size() == 0)
            return true;

        return false;
    }
}
