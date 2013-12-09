package ru.spbau.mit.dbmsau.table;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ClauseIterator implements Iterator<RecordClause>,
                                       Iterable<RecordClause> {

    private final List<String> columns;
    private final List<String> signs;
    private final List<String> values;
    private int counter;

    public ClauseIterator() {
        columns = new ArrayList<>();
        signs = new ArrayList<>();
        values = new ArrayList<>();
    }

    public ClauseIterator(List<String> columns, List<String> signs, List<String> values) {

        this.columns = columns;
        this.signs = signs;
        this.values = values;
        this.counter = 0;
    }

    public List<String> getColumns() {
        return columns;
    }

    public List<String> getSigns() {
        return signs;
    }

    public List<String> getValues() {
        return values;
    }

    public int size() {
        return columns.size();
    }

    @Override
    public Iterator<RecordClause> iterator() {
        counter = 0;
        return this;
    }

    @Override
    public boolean hasNext() {
        return counter < this.size();
    }

    @Override
    public RecordClause next() {
        String column = columns.get(counter);
        String sign   = signs.get(counter);
        String value  = values.get(counter);

        RecordClause clause = new RecordClause(column, sign, value);
        counter++;

        return clause;
    }

    @Override
    public void remove() {

    }
}
