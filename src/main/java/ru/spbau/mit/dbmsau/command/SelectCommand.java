package ru.spbau.mit.dbmsau.command;

import ru.spbau.mit.dbmsau.command.exception.CommandExecutionException;

public class SelectCommand extends AbstractSQLCommand {
    private String table;

    public SelectCommand(String table) {
        this.table = table;
    }

    public String getTable() {
        return table;
    }

    public SQLCommandResult execute() throws CommandExecutionException {
        return new SQLCommandResult();
    }
}
