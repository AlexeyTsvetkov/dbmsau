package ru.spbau.mit.dbmsau.command.create_table;

import ru.spbau.mit.dbmsau.command.AbstractSQLCommand;
import ru.spbau.mit.dbmsau.command.SQLCommandResult;
import ru.spbau.mit.dbmsau.command.exception.CommandExecutionException;
import ru.spbau.mit.dbmsau.table.Column;

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

        System.out.println("Creating table: " + tableName);

        for (Column column : columns) {
            System.out.println(column.toString());
        }

        return new SQLCommandResult();
    }
}
