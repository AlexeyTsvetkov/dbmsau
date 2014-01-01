package ru.spbau.mit.dbmsau.command;

import ru.spbau.mit.dbmsau.command.exception.CommandExecutionException;

import java.util.List;

public class CreateIndexCommand extends AbstractSQLCommand {
    private String name;
    private String tableName;
    private List<String> columns;

    public CreateIndexCommand(String name, String tableName, List<String> columns) {
        this.name = name;
        this.tableName = tableName;
        this.columns = columns;
    }

    @Override
    public SQLCommandResult execute() throws CommandExecutionException {
        getContext().getIndexManager().createIndex(name, getTable(tableName), columns);
        return new SQLCommandResult();
    }
}
