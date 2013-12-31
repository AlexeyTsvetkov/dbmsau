package ru.spbau.mit.dbmsau.relation;

public abstract class RelationRecord {
    private Relation relation;

    protected RelationRecord(Relation relation) {
        this.relation = relation;
    }

    public String getValueAsString(int columnIndex) {
        Type type = relation.getColumnType(columnIndex);

        if (type.getType() == Type.TYPE_INTEGER) {
            return Integer.valueOf(getInteger(columnIndex)).toString();
        } else if (type.getType() == Type.TYPE_VARCHAR) {
            return getString(columnIndex);
        }

        return null;
    }

    public void setValueFromString(int columnIndex, String value) {
        Type type = relation.getColumnType(columnIndex);

        if (type.getType() == Type.TYPE_INTEGER) {
            setValue(columnIndex, Integer.valueOf(value));
        } else if (type.getType() == Type.TYPE_VARCHAR) {
            setValue(columnIndex, value);
        }

    }

    public Relation getRelation() {
        return relation;
    }

    public String[] getValues() {
        String[] result = new String[relation.getColumnsCount()];

        for (int i = 0; i < result.length; i++) {
            result[i] = getValueAsString(i);
        }

        return result;
    }

    public void copyValuesFrom(RelationRecord record) {
        for (int i = 0; i < relation.getColumnsCount(); i++) {
            setValueFromString(i, record.getValueAsString(i));
        }
    }

    abstract public int getInteger(int columnIndex);

    abstract public String getString(int columnIndex);

    abstract public void setValue(int columnIndex, String value);

    abstract public void setValue(int columnIndex, int value);
}
