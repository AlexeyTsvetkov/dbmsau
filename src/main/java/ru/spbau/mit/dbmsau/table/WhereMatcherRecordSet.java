package ru.spbau.mit.dbmsau.table;

import java.util.Iterator;

public class WhereMatcherRecordSet extends RecordSet {
    private RecordSet content;
    private WhereMatcher matcher;
    private TableRecord nextResult;

    public WhereMatcherRecordSet(RecordSet content, WhereMatcher matcher) {
        this.content = content;
        this.matcher = matcher;
    }

    @Override
    public Iterator<TableRecord> iterator() {
        content.iterator();
        return this;
    }

    private void walkUntilNext() {
        while (nextResult == null && content.hasNext()) {
            TableRecord candidate = content.next();
            if (matcher.matches(candidate)) {
                nextResult = candidate;
            }
        }
    }

    @Override
    public boolean hasNext() {
        walkUntilNext();
        return nextResult != null;
    }

    @Override
    public TableRecord next() {
        walkUntilNext();

        TableRecord currentResult = nextResult;
        nextResult = null;

        return currentResult;
    }

    @Override
    public void remove() {
        content.remove();
    }
}
