package ru.spbau.mit.dbmsau.relation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

abstract public class Relation {
    protected ArrayList<Column> columns;
    protected Map<String, Integer> columnsIndexes = new HashMap<>();
    protected Map<String, Integer> tableColumnIndexes = new HashMap<>();

    protected Relation(ArrayList<Column> columns) {
        this.columns = columns;

        buildColumnsIndexes();
    }

    private void buildColumnsIndexes() {
        int index = 0;
        for (Column column : columns) {
            columnsIndexes.put(column.getName(), index);

            if (column.getTableName() != null) {
                tableColumnIndexes.put(column.getTableName() + "." + column.getName(), index);
            }

            index++;
        }
    }

    public Type getColumnType(int columnIndex) {
        return columns.get(columnIndex).getType();
    }

    public Integer getColumnIndex(String name) {
        return columnsIndexes.get(name);
    }

    public Integer getColumnIndex(String tableName, String columnName) {
        return tableColumnIndexes.get(tableName + "." + columnName);
    }

    public boolean hasColumn(String name) {
        return getColumnIndex(name) != null;
    }

    public int getColumnsCount() {
        return columns.size();
    }

    public int[] getColumnIndexesByNames(List<String> columns) {
        int[] columnIndexes = new int[columns.size()];
        int i = 0;

        for (String name : columns) {
            columnIndexes[i++] = getColumnIndex(name);
        }

        return columnIndexes;
    }

    public String getColumnName(int columnIndex) {
        return columns.get(columnIndex).getName();
    }

    public String getColumnDescription(int columnIndex) {
        return columns.get(columnIndex).toString();
    }

    public Iterable<Column> getColumns() {
        return columns;
    }
}
