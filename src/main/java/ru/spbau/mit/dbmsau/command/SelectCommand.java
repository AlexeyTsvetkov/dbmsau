package ru.spbau.mit.dbmsau.command;

import ru.spbau.mit.dbmsau.command.exception.CommandExecutionException;
import ru.spbau.mit.dbmsau.relation.RecordSet;
import ru.spbau.mit.dbmsau.relation.RelationRecord;
import ru.spbau.mit.dbmsau.relation.WhereMatcher;
import ru.spbau.mit.dbmsau.table.Table;

import java.util.Iterator;
import java.util.LinkedList;

public class SelectCommand extends AbstractSQLCommand {
    private String table;
    private WhereMatcher matcher;

    public SelectCommand(String table, WhereMatcher matcher) {
        this.table = table;
        this.matcher = matcher;
    }

    public String getTableName() {
        return table;
    }

    public SQLCommandResult execute() throws CommandExecutionException {
        Table table = getTable(getTableName());
        RecordSet result = getContext().getTableRecordManager().select(table, matcher);

        return new SQLCommandResult(new RecordSetCSVIterator(result));
    }

    private class RecordSetCSVIterator implements Iterator<String> {
        private RecordSet recordSet;
        private boolean wasHeader = false;

        private RecordSetCSVIterator(RecordSet recordSet) {
            this.recordSet = recordSet;

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

                for (int i = 0; i < recordSet.getRelation().getColumnsCount(); i++) {
                    header.add(recordSet.getRelation().getColumnName(i));
                }

                return joinCSV(header);
            }

            RelationRecord record = recordSet.next();

            LinkedList<String> row = new LinkedList<>();

            for (int i = 0; i < recordSet.getRelation().getColumnsCount(); i++) {
                row.add(record.getValueAsString(i));
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
