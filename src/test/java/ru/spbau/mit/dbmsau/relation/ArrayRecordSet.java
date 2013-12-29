package ru.spbau.mit.dbmsau.relation;

public class ArrayRecordSet extends RecordSet {
    private RelationRecord[] records;
    private int currentIndex = -1;

    public ArrayRecordSet(Relation relation, RelationRecord... records) {
        super(relation);
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
