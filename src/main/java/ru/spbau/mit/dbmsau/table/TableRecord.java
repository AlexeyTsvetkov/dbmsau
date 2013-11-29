package ru.spbau.mit.dbmsau.table;

import ru.spbau.mit.dbmsau.pages.Record;

public class TableRecord {
    private Record record;
    private Table table;

    public TableRecord(Record record, Table table) {
        this.record = record;
        this.table = table;
    }

    public void setValue(String column, String value) {
        int columnNumber = table.getColumnNumberByName(column);
        int columnOffset = table.getColumnOffset(columnNumber);

        Type type = table.getColumnTypeByNumber(columnNumber);

        if (type.getType() == Type.TYPE_INTEGER) {
             record.setIntegerValue(columnOffset, Integer.valueOf(value));
        } else if (type.getType() == Type.TYPE_VARCHAR) {
            record.setStringValue(columnOffset, value, type.getLength());
        }
    }

    public String getValueAsString(String column) {
        int columnNumber = table.getColumnNumberByName(column);
        int columnOffset = table.getColumnOffset(columnNumber);

        Type type = table.getColumnTypeByNumber(columnNumber);

        if (type.getType() == Type.TYPE_INTEGER) {
            return Integer.valueOf(record.getIntegerValue(columnOffset)).toString();
        } else if (type.getType() == Type.TYPE_VARCHAR) {
            return record.getStringValue(columnOffset, type.getLength());
        }

        return null;
    }
}
