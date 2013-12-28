package ru.spbau.mit.dbmsau.relation;

abstract public class RecordSet {
    private Relation relation;

    protected RecordSet(Relation relation) {
        this.relation = relation;
    }

    public Relation getRelation() {
        return relation;
    }

    abstract public void moveFirst();

    abstract public boolean hasNext();

    abstract public RelationRecord next();

    abstract public void remove();

    public String toString() {
        return "";
    }
}
