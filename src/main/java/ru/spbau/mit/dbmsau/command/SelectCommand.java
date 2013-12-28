package ru.spbau.mit.dbmsau.command;

import ru.spbau.mit.dbmsau.command.exception.CommandExecutionException;
import ru.spbau.mit.dbmsau.command.where.WhereExpression;
import ru.spbau.mit.dbmsau.relation.ColumnAccessor;
import ru.spbau.mit.dbmsau.relation.RecordSet;
import ru.spbau.mit.dbmsau.relation.Relation;
import ru.spbau.mit.dbmsau.relation.RelationRecord;
import ru.spbau.mit.dbmsau.table.Table;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class SelectCommand extends AbstractSQLCommand {
    private List<ColumnAccessor> columnAccessors;
    private String table;
    private WhereExpression where;

    public SelectCommand(List<ColumnAccessor> columnAccessors, String table, WhereExpression where) {
        this.columnAccessors = columnAccessors;
        this.table = table;
        this.where = where;
    }

    public String getTableName() {
        return table;
    }

    private List<ColumnAccessor> getColumnAccessors() {
        return columnAccessors;
    }

    private int[] buildColumnsIndexesToSelect(Relation relation) {
        int[] columnsIndexesToSelect;

        if (getColumnAccessors() != null) {
            getContext().getSemanticValidator().checkColumnsAccessors(relation, getColumnAccessors());

            columnsIndexesToSelect = new int[getColumnAccessors().size()];
            int index = 0;

            for (ColumnAccessor accessor : getColumnAccessors()) {
                columnsIndexesToSelect[index++] = accessor.getColumnIndex(relation);
            }
        } else {
            columnsIndexesToSelect = new int[relation.getColumnsCount()];

            for (int i = 0; i < columnsIndexesToSelect.length; i++) {
                columnsIndexesToSelect[i] = i;
            }
        }

        return columnsIndexesToSelect;
    }

    public SQLCommandResult execute() throws CommandExecutionException {
        Table table = getTable(getTableName());

        where.prepareFor(table);

        RecordSet result = getContext().getTableRecordManager().select(table, where);

        return new SQLCommandResult(new RecordSetCSVIterator(result, buildColumnsIndexesToSelect(table)));
    }

    private class RecordSetCSVIterator implements Iterator<String> {
        private RecordSet recordSet;
        private boolean wasHeader = false;
        private int[] columnsIndexesToSelect;

        private RecordSetCSVIterator(RecordSet recordSet, int[] columnsIndexesToSelect) {
            this.recordSet = recordSet;
            this.columnsIndexesToSelect = columnsIndexesToSelect;

            recordSet.moveFirst();
        }

        @Override
        public boolean hasNext() {
            if (!wasHeader) {
                return true;
            }

            return recordSet.hasNext();
        }

        @Override
        public String next() {
            if (!wasHeader) {
                wasHeader = true;

                LinkedList<String> header = new LinkedList<>();

                for (int columnIndex : columnsIndexesToSelect) {
                    header.add(recordSet.getRelation().getColumnDescription(columnIndex));
                }

                return joinCSV(header);
            }

            RelationRecord record = recordSet.next();

            LinkedList<String> row = new LinkedList<>();

            for (int columnIndex : columnsIndexesToSelect) {
                row.add(record.getValueAsString(columnIndex));
            }

            return joinCSV(row);
        }

        private String joinCSV(Iterable<String> columns) {
            StringBuilder builder = new StringBuilder("");

            for (String s : columns) {
                if (builder.length() > 0) {
                    builder.append(";");
                }

                builder.append(s);
            }

            return builder.toString();
        }

        @Override
        public void remove() {

        }
    }
}
