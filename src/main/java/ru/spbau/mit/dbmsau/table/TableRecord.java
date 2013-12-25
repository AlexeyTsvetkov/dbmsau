package ru.spbau.mit.dbmsau.table;

import ru.spbau.mit.dbmsau.pages.Record;

public class TableRecord {
    private Record record;
    private Table table;

    public TableRecord(Record record, Table table) {
        this.record = record;
        this.table = table;
    }

    public void setValue(int columnIndex, String value) {
        int columnOffset = table.getColumnOffset(columnIndex);

        Type type = table.getColumnTypeByNumber(columnIndex);

        if (type.getType() == Type.TYPE_INTEGER) {
            setIntegerValue(columnIndex, Integer.valueOf(value));
        } else if (type.getType() == Type.TYPE_VARCHAR) {
            record.setStringValue(columnOffset, value, type.getLength());
        }
    }

    public void setIntegerValue(int columnIndex, int value) {
        int columnOffset = table.getColumnOffset(columnIndex);
        record.setIntegerValue(columnOffset, value);
    }

    public void setValue(String column, String value) {
        int columnIndex = table.getColumnIndex(column);
        setValue(columnIndex, value);
    }

    public String getValueAsString(String column) {
        int columnIndex = table.getColumnIndex(column);
        int columnOffset = table.getColumnOffset(columnIndex);

        Type type = table.getColumnTypeByNumber(columnIndex);

        if (type.getType() == Type.TYPE_INTEGER) {
            return Integer.valueOf(getIntegerValue(columnIndex)).toString();
        } else if (type.getType() == Type.TYPE_VARCHAR) {
            return record.getStringValue(columnOffset, type.getLength());
        }

        return null;
    }

    public int getIntegerValue(int columnIndex) {
        int columnOffset = table.getColumnOffset(columnIndex);

        return record.getIntegerValue(columnOffset);
    }

    public Record getRecord() {
        return record;
    }
}
