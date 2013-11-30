package ru.spbau.mit.dbmsau.command;

import ru.spbau.mit.dbmsau.command.exception.CommandExecutionException;

public class DeleteCommand extends AbstractSQLCommand {
    private String tableName;

    public DeleteCommand(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }

    @Override
    public SQLCommandResult execute() throws CommandExecutionException {

        getContext().getRecordManager().delete(getTable(getTableName()));

        return new SQLCommandResult();
    }
}
