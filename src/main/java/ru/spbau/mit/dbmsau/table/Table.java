package ru.spbau.mit.dbmsau.table;

import java.util.ArrayList;
import java.util.HashMap;

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

    public Integer getColumnNumberByName(String name) {
        return columnsMap.get(name);
    }

    public boolean hasColumn(String name) {
        return getColumnNumberByName(name) != null;
    }

    public Column getColumnByName(String name) {
        Integer number = getColumnNumberByName(name);

        if (number == null) {
            return null;
        }

        return columns.get(number);
    }

    public int getColumnOffset(int number) {
        return columnsOffsets[number];
    }

    public int getColumnOffsetByName(String name) {
        return getColumnOffset(getColumnNumberByName(name));
    }

    public int getFullPagesListHeadPageId() {
        return fullPagesListHeadPageId;
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
}
