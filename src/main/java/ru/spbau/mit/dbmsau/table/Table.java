package ru.spbau.mit.dbmsau.table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Table {
    private String name;
    private ArrayList< Column > columns;
    private int fullPagesListHeadPageId;
    private int notFullPagesListHeadPageId;

    private HashMap< String, Integer > columnsMap = new HashMap<>();
    private int[] columnsOffsets;
    private int recordSize;

    public Table(String name, ArrayList<Column> columns) {
        this.name = name;
        this.columns = columns;

        initColumns();
    }

    public Table(String name, ArrayList<Column> columns, int fullPageListHeadPageId, int notFullPageListHeadPageId) {
        this(name, columns);
        this.fullPagesListHeadPageId = fullPageListHeadPageId;
        this.notFullPagesListHeadPageId = notFullPageListHeadPageId;
    }

    private void initColumns() {
        for (int i = 0; i < columns.size(); i++) {
            columnsMap.put(columns.get(i).getName().toLowerCase(), i);
        }

        columnsOffsets = new int[columns.size()];
        columnsOffsets[0] = 0;

        for (int i = 1; i < columns.size(); i++) {
            columnsOffsets[i] = columnsOffsets[i-1] + columns.get(i-1).getType().getSize();
        }

        recordSize = columnsOffsets[columns.size()-1] + columns.get(columns.size()-1).getType().getSize();

    }

    public String getName() {
        return name;
    }

    public ArrayList<Column> getColumns() {
        return columns;
    }

    public List<String> getColumnsNames() {
        List<String> names = new ArrayList<>(columns.size());
        for(Column column : columns) {
            names.add(column.getName());
        }

        return names;
    }

    public int getRecordSize() {
        return recordSize;
    }

    public Integer getColumnIndex(String name) {
        return columnsMap.get(name);
    }

    public boolean hasColumn(String name) {
        return getColumnIndex(name) != null;
    }

    public Column getColumn(String name) {
        Integer number = getColumnIndex(name);

        if (number == null) {
            return null;
        }

        return columns.get(number);
    }

    public int getColumnOffset(int number) {
        return columnsOffsets[number];
    }

    public Type getColumnTypeByNumber(int number) {
        return columns.get(number).getType();
    }

    public Type getColumnType(String name) {
        int index = getColumnIndex(name);
        return columns.get(index).getType();
    }

    public int getColumnOffset(String name) {
        return getColumnOffset(getColumnIndex(name));
    }

    public int getFullPagesListHeadPageId() {
        return fullPagesListHeadPageId;
    }

    public int[] getColumnIndexesByNames(List<String> columns) {
        int[] columnIndexes = new int[columns.size()];
        int i = 0;

        for (String name : columns) {
            columnIndexes[i++] = getColumnIndex(name);
        }

        return columnIndexes;
    }

    public int getNotFullPagesListHeadPageId() {
        return notFullPagesListHeadPageId;
    }

    public void setFullPagesListHeadPageId(int fullPagesListHeadPageId) {
        this.fullPagesListHeadPageId = fullPagesListHeadPageId;
    }

    public void setNotFullPagesListHeadPageId(int notFullPagesListHeadPageId) {
        this.notFullPagesListHeadPageId = notFullPagesListHeadPageId;
    }

    public int getColumnsCount() {
        return getColumns().size();
    }
}
