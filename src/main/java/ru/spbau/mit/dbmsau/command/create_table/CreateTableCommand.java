package ru.spbau.mit.dbmsau.command.create_table;

import ru.spbau.mit.dbmsau.command.AbstractSQLCommand;
import ru.spbau.mit.dbmsau.command.SQLCommandResult;
import ru.spbau.mit.dbmsau.command.exception.CommandExecutionException;

import java.util.List;

public class CreateTableCommand extends AbstractSQLCommand {
    private String tableName;
    private List< ColumnDescription > columns;

    public CreateTableCommand(String tableName, List<ColumnDescription> columns) {
        this.tableName = tableName;
        this.columns = columns;
    }

    public String getTableName() {
        return tableName;
    }

    public List<ColumnDescription> getColumns() {
        return columns;
    }

    @Override
    public SQLCommandResult execute() throws CommandExecutionException {

        System.out.println("Creating table: " + tableName);

        for (ColumnDescription column : columns) {
            System.out.println(column.toString());
        }

        return new SQLCommandResult();
    }
}
