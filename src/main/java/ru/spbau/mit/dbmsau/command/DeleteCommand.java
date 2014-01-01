package ru.spbau.mit.dbmsau.command;

import ru.spbau.mit.dbmsau.command.exception.CommandExecutionException;
import ru.spbau.mit.dbmsau.command.where.WhereExpression;
import ru.spbau.mit.dbmsau.relation.RecordSet;

public class DeleteCommand extends ConditionalCommand {
    private String tableName;

    public DeleteCommand(String tableName, WhereExpression where) {
        super(where);
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }

    @Override
    public SQLCommandResult execute() throws CommandExecutionException {
        RecordSet recordSet = createAppropriateFilteredRecordSet(getTable(getTableName()));
        recordSet.moveFirst();

        int rows = 0;

        while (recordSet.hasNext()) {
            recordSet.next();
            recordSet.remove();
            rows++;
        }

        return new SQLCommandResult(rows);
    }
}
