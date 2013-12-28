package ru.spbau.mit.dbmsau.relation;

public interface WhereMatcher {
    public boolean matches(RelationRecord record);

    public void prepareFor(Relation relation);
}
