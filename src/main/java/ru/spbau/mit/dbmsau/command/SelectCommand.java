package ru.spbau.mit.dbmsau.command;

import ru.spbau.mit.dbmsau.command.exception.CommandExecutionException;
import ru.spbau.mit.dbmsau.command.where.WhereExpression;
import ru.spbau.mit.dbmsau.index.IndexJoinRecordSet;
import ru.spbau.mit.dbmsau.relation.*;
import ru.spbau.mit.dbmsau.table.SemanticValidator;
import ru.spbau.mit.dbmsau.table.Table;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class SelectCommand extends ConditionalCommand {
    private List<ColumnAccessor> columnAccessors;
    private String table;
    private JoinDescription join;

    public SelectCommand(List<ColumnAccessor> columnAccessors, String table, WhereExpression where) {
        super(where);
        this.columnAccessors = columnAccessors;
        this.table = table;
    }

    public SelectCommand(List<ColumnAccessor> columnAccessors, String table, WhereExpression where, JoinDescription join) {
        this(columnAccessors, table, where);
        this.join = join;
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
            columnsIndexesToSelect = getColumnIndexesOf(relation, getColumnAccessors());
        } else {
            columnsIndexesToSelect = new int[relation.getColumnsCount()];

            for (int i = 0; i < columnsIndexesToSelect.length; i++) {
                columnsIndexesToSelect[i] = i;
            }
        }

        return columnsIndexesToSelect;
    }

    private boolean tableHasIndexForColumn(Table table, int columnIndex) {
        return getContext().getIndexManager().findAppropriateIndex(table, new int[]{columnIndex}) != null;
    }

    private RecordSet createJoinRecordSet(Table table1, Table table2) {
        SemanticValidator validator = getContext().getSemanticValidator();
        validator.checkColumnAccessor(table1, join.getLeft());
        validator.checkColumnAccessor(table2, join.getRight());

        int leftColumnIndex = join.getLeft().getColumnIndex(table1);
        int rightColumnIndex = join.getRight().getColumnIndex(table2);

        if (tableHasIndexForColumn(table1, leftColumnIndex)) {
            //very sad :(
            Table tmp = table1;
            table1 = table2;
            table2 = tmp;

            int tmpInt = leftColumnIndex;
            leftColumnIndex = rightColumnIndex;
            rightColumnIndex = tmpInt;
        }

        JoinedRelation relation = new JoinedRelation(
            table1, table2,
            leftColumnIndex, rightColumnIndex
        );

        RecordSet leftPart = getContext().getTableRecordManager().select(table1);

        if (tableHasIndexForColumn(table2, rightColumnIndex)) {
            return new IndexJoinRecordSet(
                relation,
                leftPart,
                getContext().getIndexManager().findAppropriateIndex(table2, new int[]{rightColumnIndex})
            );
        } else {
            return new NestedLoopJoinRecordSet(
                relation,
                leftPart,
                getContext().getTableRecordManager().select(table2)
            );
        }
    }

    private RecordSet prepareRecordSet() {
        Table table = getTable(getTableName());
        RecordSet result;

        if (join != null) {
            result = createJoinRecordSet(table, getTable(join.getTableName()));
        } else {
            result = createAppropriateRecordSet(table);
        }

        return result;
    }

    public SQLCommandResult execute() throws CommandExecutionException {
        RecordSet result = filterRecordSet(prepareRecordSet());

        return new SQLCommandResult(
            new RecordSetCSVIterator(
                result,
                buildColumnsIndexesToSelect(result.getRelation())
            )
        );
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
