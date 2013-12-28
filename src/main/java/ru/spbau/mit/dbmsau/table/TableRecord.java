package ru.spbau.mit.dbmsau.table;

import ru.spbau.mit.dbmsau.pages.Record;
import ru.spbau.mit.dbmsau.relation.RelationRecord;
import ru.spbau.mit.dbmsau.relation.Type;

public class TableRecord extends RelationRecord {
    private Record record;
    private Table table;

    public TableRecord(Record record, Table table) {
        super(table);
        this.record = record;
        this.table = table;
    }

    public void setValue(int columnIndex, String value) {
        int columnOffset = table.getColumnOffset(columnIndex);

        Type type = table.getColumnType(columnIndex);
        assert type.getType() == Type.TYPE_VARCHAR;

        record.setStringValue(columnOffset, value, type.getLength());
    }

    public void setValue(int columnIndex, int value) {
        int columnOffset = table.getColumnOffset(columnIndex);

        assert table.getColumnType(columnIndex).getType() == Type.TYPE_INTEGER;

        record.setIntegerValue(columnOffset, value);
    }

    @Override
    public int getInteger(int columnIndex) {
        int columnOffset = table.getColumnOffset(columnIndex);

        return record.getIntegerValue(columnOffset);
    }

    @Override
    public String getString(int columnIndex) {
        return record.getStringValue(
            getColumnOffset(columnIndex),
            getColumnLength(columnIndex)
        );
    }

    public Record getRecord() {
        return record;
    }

    private int getColumnOffset(int columnIndex) {
        return table.getColumnOffset(columnIndex);
    }

    private int getColumnLength(int columnIndex) {
        return table.getColumnType(columnIndex).getLength();
    }
}
