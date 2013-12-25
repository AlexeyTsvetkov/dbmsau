package ru.spbau.mit.dbmsau.index;

import ru.spbau.mit.dbmsau.table.RecordSet;
import ru.spbau.mit.dbmsau.table.Table;

import java.util.List;

abstract public class Index {
    public static final int EQUALITY_MATCHING_TYPE = 0;

    protected String name;
    protected Table table;
    protected int[] columnIndexes;

    protected Index(String name, Table table, int[] columnIndexes) {
        this.name = name;
        this.table = table;
        this.columnIndexes = columnIndexes;
    }

    public String getName() {
        return name;
    }

    public Table getTable() {
        return table;
    }

    public int[] getColumnIndexes() {
        return columnIndexes;
    }

    abstract public boolean isMatchingFor(int[] queryColumnIndexes, int matchingType);
    abstract public RecordSet buildRecordSetMatchingEqualityCondition(int[] queryColumnIndexes, String[] values);
}
