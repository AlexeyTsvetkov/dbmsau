package ru.spbau.mit.dbmsau.table;

import ru.spbau.mit.dbmsau.relation.RecordSet;
import ru.spbau.mit.dbmsau.relation.RelationRecord;

public class ArrayRecordSet extends RecordSet {
    private RelationRecord[] records;
    private int currentIndex = 0;

    public ArrayRecordSet(RelationRecord[] records) {
        super(null);
        this.records = records;
    }

    @Override
    public void moveFirst() {
        currentIndex = 0;
    }

    @Override
    public boolean hasNext() {
        return currentIndex < records.length;
    }

    @Override
    public RelationRecord next() {
        return records[currentIndex++];
    }

    @Override
    public void remove() {
    }
}
