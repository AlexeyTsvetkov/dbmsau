package ru.spbau.mit.dbmsau.table;

import ru.spbau.mit.dbmsau.Context;
import ru.spbau.mit.dbmsau.table.exception.TableManagerException;

public class StubTableManager extends TableManager {

    public StubTableManager(Context context) {
        super(context);
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void createNewTable(Table table) throws TableManagerException {
        tables.put(table.getName(), table);
    }
}
