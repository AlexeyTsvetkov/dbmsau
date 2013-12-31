package ru.spbau.mit.dbmsau.relation;

public class MemoryRelationRecord extends RelationRecord {
    String[] columnsValues;

    public MemoryRelationRecord(Relation relation) {
        super(relation);
        columnsValues = new String[relation.getColumnsCount()];

        for (int i = 0; i < columnsValues.length; i++) {
            columnsValues[i] = "";
        }
    }

    public MemoryRelationRecord(Relation relation, String... columnsValues) {
        this(relation);
        this.columnsValues = columnsValues;
    }

    public MemoryRelationRecord(RelationRecord record) {
        this(record.getRelation(), record.getValues());
    }

    @Override
    public void setValue(int columnIndex, String value) {
        columnsValues[columnIndex] = value;
    }

    @Override
    public int getInteger(int columnIndex) {
        return Integer.valueOf(columnsValues[columnIndex]);
    }

    @Override
    public String getString(int columnIndex) {
        return columnsValues[columnIndex];
    }

    @Override
    public void setValue(int columnIndex, int value) {
        setValue(columnIndex, Integer.valueOf(value).toString());
    }
}
