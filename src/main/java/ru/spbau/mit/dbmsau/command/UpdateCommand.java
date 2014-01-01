package ru.spbau.mit.dbmsau.command;

import ru.spbau.mit.dbmsau.command.exception.CommandExecutionException;
import ru.spbau.mit.dbmsau.command.where.WhereExpression;
import ru.spbau.mit.dbmsau.relation.ColumnAccessor;
import ru.spbau.mit.dbmsau.relation.MemoryRelationRecord;
import ru.spbau.mit.dbmsau.relation.RecordSet;
import ru.spbau.mit.dbmsau.table.Table;
import ru.spbau.mit.dbmsau.table.exception.RecordManagerException;

import java.util.List;

public class UpdateCommand extends ConditionalCommand {
    private String tableName;
    private List<ColumnAccessor> columnAccessors;
    private List<String> values;

    public UpdateCommand(String tableName, List<ColumnAccessor> columnAccessors, List<String> values, WhereExpression where) {
        super(where);
        this.tableName = tableName;
        this.columnAccessors = columnAccessors;
        this.values = values;
    }

    @Override
    protected SQLCommandResult doExecute() throws CommandExecutionException {
        Table table = getTable(tableName);

        int[] changesColumnIndexes = getColumnIndexesOf(table, columnAccessors);

        RecordSet recordSet = createAppropriateFilteredRecordSet(table);
        recordSet.moveFirst();

        MemoryRelationRecord record = new MemoryRelationRecord(table);

        int rows = 0;

        while (recordSet.hasNext()) {
            rows++;
            record.copyValuesFrom(recordSet.next());

            recordSet.remove();

            for (int i = 0; i < changesColumnIndexes.length; i++) {
                record.setValueFromString(changesColumnIndexes[i], values.get(i));
            }

            try {
                getContext().getTableRecordManager().insert(table, record);
            } catch (RecordManagerException e) {
                throw new CommandExecutionException(e.getMessage());
            }
        }

        return new SQLCommandResult(rows);
    }
}
