package ru.spbau.mit.dbmsau.table;

import java.util.ArrayList;

public class Table {
    private String name;
    private ArrayList< Column > columns;
    private Integer fullPagesListHeadPageId;
    private Integer notFullPagesListHeadPageId;

    public Table(String name, ArrayList<Column> columns, Integer fullPageListHeadPageId, Integer notFullPageListHeadPageId) {
        this.name = name;
        this.columns = columns;
        this.fullPagesListHeadPageId = fullPageListHeadPageId;
        this.notFullPagesListHeadPageId = notFullPageListHeadPageId;
    }

    public Table(String name, ArrayList<Column> columns) {
        this.name = name;
        this.columns = columns;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Column> getColumns() {
        return columns;
    }

    public Integer getFullPagesListHeadPageId() {
        return fullPagesListHeadPageId;
    }

    public Integer getNotFullPagesListHeadPageId() {
        return notFullPagesListHeadPageId;
    }

    public void setFullPagesListHeadPageId(Integer fullPagesListHeadPageId) {
        this.fullPagesListHeadPageId = fullPagesListHeadPageId;
    }

    public void setNotFullPagesListHeadPageId(Integer notFullPagesListHeadPageId) {
        this.notFullPagesListHeadPageId = notFullPagesListHeadPageId;
    }
}
