package ru.spbau.mit.dbmsau.command;

import ru.spbau.mit.dbmsau.pages.Record;
import ru.spbau.mit.dbmsau.table.RecordComparisonClause;
import ru.spbau.mit.dbmsau.table.WhereMatcher;

import java.util.List;

public class TableRecordMatcher implements WhereMatcher {

    private List<RecordComparisonClause> clauses;

    TableRecordMatcher(List<RecordComparisonClause> clauses) {
        this.clauses = clauses;
    }

    @Override
    public boolean matches(Record record) {
        return true;
    }
}
