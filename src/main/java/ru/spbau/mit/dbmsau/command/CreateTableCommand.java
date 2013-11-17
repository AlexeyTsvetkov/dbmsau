package ru.spbau.mit.dbmsau.command;

import ru.spbau.mit.dbmsau.command.AbstractSQLCommand;
import ru.spbau.mit.dbmsau.command.SQLCommandResult;
import ru.spbau.mit.dbmsau.command.exception.CommandExecutionException;
import ru.spbau.mit.dbmsau.table.*;
import ru.spbau.mit.dbmsau.table.exception.TableManagerException;

import java.util.ArrayList;
import java.util.List;

public class CreateTableCommand extends AbstractSQLCommand {
    private String tableName;
    private List<Column> columns;

    public CreateTableCommand(String tableName, List<Column> columns) {
        this.tableName = tableName;
        this.columns = columns;
    }

    public String getTableName() {
        return tableName;
    }

    public List<Column> getColumns() {
        return columns;
    }

    @Override
    public SQLCommandResult execute() throws CommandExecutionException {
        Table table = new Table(getTableName(), new ArrayList<>(getColumns()));

        try {
            getContext().getTableManager().createNewTable(table);
        } catch (TableManagerException e) {
            throw new CommandExecutionException(e.getMessage());
        }

        return new SQLCommandResult();
    }
}
