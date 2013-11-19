package ru.spbau.mit.dbmsau.table;

import ru.spbau.mit.dbmsau.pages.Record;

public class TableRecord {
    private Record record;
    private Table table;

    public TableRecord(Record record, Table table) {
        this.record = record;
        this.table = table;
    }

    public void setIntegerValue(String column, Integer value) {
        record.setIntegerValue(table.getColumnOffsetByName(column), value);
    }

    public void setValue(String column, String value) {
        setIntegerValue(column, Integer.valueOf(value));
    }

    public String getIntegerValue(String column) {
        return record.getIntegerValue(table.getColumnOffsetByName(column)).toString();
    }
}
