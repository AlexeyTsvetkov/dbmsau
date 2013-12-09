package ru.spbau.mit.dbmsau.table;

import ru.spbau.mit.dbmsau.pages.Record;

public class TableRecord {
    private Record record;
    private Table table;

    public TableRecord(Record record, Table table) {
        this.record = record;
        this.table = table;
    }

    public Type getColumnType(String column) {
        return table.getColumnType(column);
    }

    public void setValue(int columnNumber, String value) {
        int columnOffset = table.getColumnOffset(columnNumber);

        Type type = table.getColumnType(columnNumber);

        if (type.getType() == Type.TYPE_INTEGER) {
            setIntegerValue(columnNumber, Integer.valueOf(value));
        } else if (type.getType() == Type.TYPE_VARCHAR) {
            record.setStringValue(columnOffset, value, type.getLength());
        }
    }

    public void setIntegerValue(int columnNumber, int value) {
        int columnOffset = table.getColumnOffset(columnNumber);
        record.setIntegerValue(columnOffset, value);
    }

    public void setValue(String column, String value) {
        int columnNumber = table.getColumnIndex(column);
        setValue(columnNumber, value);
    }

    public String getValueAsString(String column) {
        int columnNumber = table.getColumnIndex(column);
        int columnOffset = table.getColumnOffset(columnNumber);

        Type type = table.getColumnType(columnNumber);

        if (type.getType() == Type.TYPE_INTEGER) {
            return Integer.valueOf(getIntegerValue(columnNumber)).toString();
        } else if (type.getType() == Type.TYPE_VARCHAR) {
            return record.getStringValue(columnOffset, type.getLength());
        }

        return null;
    }

    public int getIntegerValue(int columnNumber) {
        int columnOffset = table.getColumnOffset(columnNumber);

        return record.getIntegerValue(columnOffset);
    }

    public Record getRecord() {
        return record;
    }
}
