package ru.spbau.mit.dbmsau.syntax;

import ru.spbau.mit.dbmsau.relation.Relation;
import ru.spbau.mit.dbmsau.relation.RelationRecord;
import ru.spbau.mit.dbmsau.table.Table;

public class StubRelationRecord extends RelationRecord {
    String[] columnsValues;

    public StubRelationRecord(Relation relation) {
        super(relation);
        columnsValues = new String[relation.getColumnsCount()];
    }

    public StubRelationRecord(Table table, String[] columnsValues) {
        this(table);
        this.columnsValues = columnsValues;
    }

    @Override
    public void setValue(int columnIndex, String value) {
        columnsValues[columnIndex] = value;
    }

    @Override
    public int getInteger(int columnIndex) {
        return Integer.valueOf(columnsValues[columnIndex]);
    }

    @Override
    public String getString(int columnIndex) {
        return columnsValues[columnIndex];
    }

    @Override
    public void setValue(int columnIndex, int value) {
        setValue(columnIndex, Integer.valueOf(value).toString());
    }
}
