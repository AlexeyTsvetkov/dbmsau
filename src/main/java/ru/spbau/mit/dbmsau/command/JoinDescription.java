package ru.spbau.mit.dbmsau.command;

import ru.spbau.mit.dbmsau.relation.ColumnAccessor;

public class JoinDescription {
    private String tableName;
    private ColumnAccessor left;
    private ColumnAccessor right;

    public JoinDescription(String tableName, ColumnAccessor left, ColumnAccessor right) {
        this.tableName = tableName;
        this.left = left;
        this.right = right;
    }

    public String getTableName() {
        return tableName;
    }

    public ColumnAccessor getLeft() {
        return left;
    }

    public ColumnAccessor getRight() {
        return right;
    }
}
