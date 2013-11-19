package ru.spbau.mit.dbmsau.command;

import ru.spbau.mit.dbmsau.Context;
import ru.spbau.mit.dbmsau.command.exception.CommandExecutionException;
import ru.spbau.mit.dbmsau.table.Table;

abstract public class AbstractSQLCommand {
    private Context context;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    protected Table getTable(String name) throws CommandExecutionException {
        Table table = getContext().getTableManager().getTable(name);

        if (table == null) {
            throw new CommandExecutionException("No such table `" +  name + "`");
        }

        return table;
    }

    abstract public SQLCommandResult execute() throws CommandExecutionException;
}
