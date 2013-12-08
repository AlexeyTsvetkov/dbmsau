package ru.spbau.mit.dbmsau.table;

import java.util.Iterator;

public class ArrayRecordSet extends RecordSet {
    private TableRecord[] records;
    private int currentIndex = 0;

    public ArrayRecordSet(TableRecord[] records) {
        this.records = records;
    }

    @Override
    public Iterator<TableRecord> iterator() {
        currentIndex = 0;
        return this;
    }

    @Override
    public boolean hasNext() {
        return currentIndex < records.length;
    }

    @Override
    public TableRecord next() {
        return records[currentIndex++];
    }

    @Override
    public void remove() {
    }
}
