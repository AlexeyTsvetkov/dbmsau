package ru.spbau.mit.dbmsau.table;

import ru.spbau.mit.dbmsau.Context;
import ru.spbau.mit.dbmsau.ContextContainer;
import ru.spbau.mit.dbmsau.table.exception.TableManagerException;

import java.util.HashMap;

abstract public class TableManager extends ContextContainer {
    protected HashMap<String, Table> tables = new HashMap<>();

    protected TableManager(Context context) {
        super(context);
    }

    public void init() {

    }

    public boolean tableExists(String name) {
        return tables.containsKey(name);
    }

    public Table getTable(String name) {
        if (tableExists(name)) {
            return tables.get(name);
        }

        return null;
    }

    abstract public void createNewTable(Table table) throws TableManagerException;
}
