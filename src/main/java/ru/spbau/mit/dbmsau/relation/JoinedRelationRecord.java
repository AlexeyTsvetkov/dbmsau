package ru.spbau.mit.dbmsau.relation;

public class JoinedRelationRecord extends RelationRecord {
    private RelationRecord first;
    private RelationRecord second;
    private int columnsCountInFirstRelation;

    public JoinedRelationRecord(JoinedRelation relation, RelationRecord first, RelationRecord second) {
        super(relation);
        this.first = first;
        this.second = second;

        columnsCountInFirstRelation = relation.getFirst().getColumnsCount();
    }

    private RelationRecord getRecordObject(int columnIndex) {
        if (columnIndex < columnsCountInFirstRelation) {
            return first;
        } else {
            return second;
        }
    }

    private int getLocalColumnIndex(int columnIndex) {
        return (columnIndex < columnsCountInFirstRelation) ? columnIndex : columnIndex - columnsCountInFirstRelation;
    }

    @Override
    public int getInteger(int columnIndex) {
        return getRecordObject(columnIndex).getInteger(getLocalColumnIndex(columnIndex));
    }

    @Override
    public String getString(int columnIndex) {
        return getRecordObject(columnIndex).getString(getLocalColumnIndex(columnIndex));
    }

    @Override
    public void setValue(int columnIndex, String value) {
        getRecordObject(columnIndex).setValue(getLocalColumnIndex(columnIndex), value);
    }

    @Override
    public void setValue(int columnIndex, int value) {
        getRecordObject(columnIndex).setValue(getLocalColumnIndex(columnIndex), value);
    }
}
