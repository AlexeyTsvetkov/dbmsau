package ru.spbau.mit.dbmsau.syntax;

import ru.spbau.mit.dbmsau.pages.Record;
import ru.spbau.mit.dbmsau.table.Table;
import ru.spbau.mit.dbmsau.table.TableRecord;

import java.util.HashMap;

public class StubTableRecord extends TableRecord {
    private Table table;
    String[] columnsValues;

    public StubTableRecord(Table table) {
        super(null, table);
        this.table = table;
        columnsValues = new String[table.getColumnsCount()];
    }

    public StubTableRecord(Table table, String[] columnsValues) {
        this(table);
        this.columnsValues = columnsValues;
    }

    @Override
    public void setValue(int columnNumber, String value) {
        columnsValues[columnNumber] = value;
    }

    @Override
    public void setIntegerValue(int columnNumber, int value) {
        setValue(columnNumber, Integer.valueOf(value).toString());
    }

    @Override
    public void setValue(String column, String value) {
        setValue(table.getColumnIndex(column), value);
    }

    @Override
    public String getValueAsString(String column) {
        return columnsValues[table.getColumnIndex(column)];
    }

    @Override
    public int getIntegerValue(int columnNumber) {
        return Integer.valueOf(getValueAsString(table.getColumns().get(columnNumber).getName()));
    }
}
