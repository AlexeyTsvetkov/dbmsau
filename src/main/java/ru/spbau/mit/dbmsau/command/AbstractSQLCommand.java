package ru.spbau.mit.dbmsau.command;

import ru.spbau.mit.dbmsau.Context;
import ru.spbau.mit.dbmsau.command.exception.CommandExecutionException;
import ru.spbau.mit.dbmsau.exception.UserError;
import ru.spbau.mit.dbmsau.relation.ColumnAccessor;
import ru.spbau.mit.dbmsau.relation.Relation;
import ru.spbau.mit.dbmsau.table.Table;

import java.util.List;

abstract public class AbstractSQLCommand {
    private Context context;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    protected Table getTable(String name) {
        Table table = getContext().getTableManager().getTable(name);

        if (table == null) {
            throw new UserError("No such table `" + name + "`");
        }

        return table;
    }

    protected int[] getColumnIndexesOf(Relation relation, List<ColumnAccessor> accessors) {
        getContext().getSemanticValidator().checkColumnsAccessors(relation, accessors);

        int[] columnsIndexes = new int[accessors.size()];
        int index = 0;

        for (ColumnAccessor accessor : accessors) {
            columnsIndexes[index++] = accessor.getColumnIndex(relation);
        }

        return columnsIndexes;
    }

    abstract public SQLCommandResult execute() throws CommandExecutionException;
}
