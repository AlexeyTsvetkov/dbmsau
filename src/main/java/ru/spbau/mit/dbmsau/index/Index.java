package ru.spbau.mit.dbmsau.index;

import ru.spbau.mit.dbmsau.table.Table;

import java.util.List;

abstract public class Index {
    protected String name;
    protected Table table;
    protected int[] columnNumbers;

    protected Index(String name, Table table, int[] columnNumbers) {
        this.name = name;
        this.table = table;
        this.columnNumbers = columnNumbers;
    }

    public String getName() {
        return name;
    }

    public Table getTable() {
        return table;
    }

    public int[] getColumnNumbers() {
        return columnNumbers;
    }
}
