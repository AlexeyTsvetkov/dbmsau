package ru.spbau.mit.dbmsau.command;

import ru.spbau.mit.dbmsau.Context;
import ru.spbau.mit.dbmsau.command.exception.CommandExecutionException;

abstract public class AbstractSQLCommand {
    private Context context;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    abstract public SQLCommandResult execute() throws CommandExecutionException;
}
