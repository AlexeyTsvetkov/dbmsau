package ru.spbau.mit.dbmsau.relation;

abstract public class BaseRecordSet extends RecordSet {
    protected BaseRecordSet(Relation relation) {
        super(relation);
    }

    private RelationRecord current = null;

    private void doMoveUntilNext() {
        if (current == null) {
            current = moveUntilNext();
        }
    }

    @Override
    public boolean hasNext() {
        doMoveUntilNext();
        return current != null;
    }

    @Override
    public RelationRecord next() {
        doMoveUntilNext();

        RelationRecord result = current;
        current = null;

        return result;
    }

    abstract protected RelationRecord moveUntilNext();
}
