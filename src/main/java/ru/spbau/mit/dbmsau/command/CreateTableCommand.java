package ru.spbau.mit.dbmsau.command;

import ru.spbau.mit.dbmsau.command.exception.CommandExecutionException;
import ru.spbau.mit.dbmsau.relation.Column;
import ru.spbau.mit.dbmsau.table.SemanticValidator;
import ru.spbau.mit.dbmsau.table.Table;
import ru.spbau.mit.dbmsau.table.TableManager;
import ru.spbau.mit.dbmsau.table.exception.SemanticError;
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
    protected SQLCommandResult doExecute() throws CommandExecutionException, SemanticError {
        Table table = new Table(getTableName(), new ArrayList<>(getColumns()));
        TableManager manager = getContext().getTableManager();

        SemanticValidator validator = new SemanticValidator();
        validator.checkCreateTable(table, manager);

        try {
            getContext().getTableManager().createNewTable(table);
        } catch (TableManagerException e) {
            throw new CommandExecutionException(e.getMessage());
        }

        return new SQLCommandResult();
    }
}
