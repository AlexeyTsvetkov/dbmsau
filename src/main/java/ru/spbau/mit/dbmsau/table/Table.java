package ru.spbau.mit.dbmsau.table;

import ru.spbau.mit.dbmsau.relation.Column;
import ru.spbau.mit.dbmsau.relation.Relation;

import java.util.ArrayList;

public class Table extends Relation {
    private String name;
    private int fullPagesListHeadPageId;
    private int notFullPagesListHeadPageId;

    private int[] columnsOffsets;
    private int recordSize;

    public Table(String name, ArrayList<Column> columns) {
        super(columns);
        this.name = name;
        initColumns();
    }

    public Table(String name, ArrayList<Column> columns, int fullPageListHeadPageId, int notFullPageListHeadPageId) {
        this(name, columns);
        this.fullPagesListHeadPageId = fullPageListHeadPageId;
        this.notFullPagesListHeadPageId = notFullPageListHeadPageId;
    }

    private void initColumns() {
        columnsOffsets = new int[columns.size()];
        columnsOffsets[0] = 0;

        for (int i = 1; i < columns.size(); i++) {
            columnsOffsets[i] = columnsOffsets[i - 1] + columns.get(i - 1).getType().getSize();
        }

        recordSize = columnsOffsets[columns.size() - 1] + columns.get(columns.size() - 1).getType().getSize();
    }

    public String getName() {
        return name;
    }

    public int getRecordSize() {
        return recordSize;
    }

    public int getColumnOffset(int index) {
        return columnsOffsets[index];
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
