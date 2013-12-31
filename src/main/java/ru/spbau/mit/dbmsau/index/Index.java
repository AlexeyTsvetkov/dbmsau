package ru.spbau.mit.dbmsau.index;

import ru.spbau.mit.dbmsau.relation.RecordSet;
import ru.spbau.mit.dbmsau.relation.RelationRecord;
import ru.spbau.mit.dbmsau.table.Table;
import ru.spbau.mit.dbmsau.table.TableRecord;

abstract public class Index {
    public static final int MATCHING_TYPE_EQUALITY = 0;
    public static final int MATCHING_TYPE_LESS = 1;
    public static final int MATCHING_TYPE_GREATER = 2;
    public static final int MATCHING_TYPE_LESS_OR_EQUAL = 3;
    public static final int MATCHING_TYPE_GREATER_OR_EQUAL = 4;


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

    public void initFirstTime() {

    }

    abstract public boolean isMatchingFor(IndexQuery query);

    abstract public RecordSet buildRecordSet(IndexQuery query);

    abstract public void processNewRecord(TableRecord record);

    abstract public boolean isDuplicateViolation(RelationRecord record);

    abstract public void processDeletedRecord(TableRecord record);


}
