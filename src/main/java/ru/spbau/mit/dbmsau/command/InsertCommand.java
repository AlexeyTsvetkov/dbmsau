package ru.spbau.mit.dbmsau.command;

import ru.spbau.mit.dbmsau.command.exception.CommandExecutionException;
import ru.spbau.mit.dbmsau.table.Table;
import ru.spbau.mit.dbmsau.table.exception.RecordManagerException;

import java.util.List;

public class InsertCommand extends AbstractSQLCommand {
    private String tableName;
    private List<String> columns;
    private List<String> values;

    public InsertCommand(String tableName, List<String> columns, List<String> values) {
        this.tableName = tableName;
        this.columns = columns;
        this.values = values;
    }

    public String getTableName() {
        return tableName;
    }

    public List<String> getColumns() {
        return columns;
    }

    public List<String> getValues() {
        return values;
    }

    @Override
    public SQLCommandResult execute() throws CommandExecutionException {
        Table table = getTable(getTableName());

        getContext().getSemanticValidator().checkTypesCompatibility(table, getColumns(), getValues());

        try {
            getContext().getRecordManager().insert(table, getColumns(), getValues());
        } catch (RecordManagerException e) {
            throw new CommandExecutionException("RME: " + e.getMessage());
        }

        return new SQLCommandResult();
    }
}
