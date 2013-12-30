package ru.spbau.mit.dbmsau.index;

import ru.spbau.mit.dbmsau.relation.RecordSet;
import ru.spbau.mit.dbmsau.relation.RelationRecord;
import ru.spbau.mit.dbmsau.table.Table;
import ru.spbau.mit.dbmsau.table.TableRecord;

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

    abstract public void processNewRecord(TableRecord record);

    abstract public boolean isDuplicateViolation(RelationRecord record);

    abstract public void processDeletedRecord(TableRecord record);


}
