package ru.spbau.mit.dbmsau.relation;

public class ColumnAccessor {
    private String tableName;
    private String columnName;

    public ColumnAccessor(String tableName, String columnName) {
        this.tableName = tableName;
        this.columnName = columnName;
    }

    public Integer getColumnIndex(Relation relation) {
        if (tableName == null) {
            return relation.getColumnIndex(columnName);
        }

        return relation.getColumnIndex(tableName, columnName);
    }

    public String toString() {
        if (tableName == null) {
            return columnName;
        }

        return tableName + "." + columnName;
    }
}
