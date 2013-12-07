package ru.spbau.mit.dbmsau.index;

import ru.spbau.mit.dbmsau.table.Table;

public class BTreeIndex extends Index {
    private int rootPageId;

    public BTreeIndex(String name, Table table, int[] columnNumbers, int rootPageId) {
        super(name, table, columnNumbers);
        this.rootPageId = rootPageId;
    }

    public int getRootPageId() {
        return rootPageId;
    }
}
