package ru.spbau.mit.dbmsau.index;

import ru.spbau.mit.dbmsau.Context;
import ru.spbau.mit.dbmsau.ContextContainer;
import ru.spbau.mit.dbmsau.table.Table;

import java.util.List;

abstract public class IndexManager extends ContextContainer {

    public IndexManager(Context context) {
        super(context);
    }

    public void init() {

    }

    abstract public void createIndex(String name, Table table, List<String> columns);
    abstract public Iterable<Index> getIndexesForTable(Table table);
}
