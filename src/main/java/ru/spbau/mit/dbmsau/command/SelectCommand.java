package ru.spbau.mit.dbmsau.command;

import ru.spbau.mit.dbmsau.command.exception.CommandExecutionException;
import ru.spbau.mit.dbmsau.table.*;
import ru.spbau.mit.dbmsau.table.exception.SemanticError;

import java.util.Iterator;
import java.util.LinkedList;

public class SelectCommand extends AbstractSQLCommand {
    private String table;
    private final ClauseIterator clauses;

    public SelectCommand(String table, ClauseIterator clauses) {
        this.table = table;
        this.clauses = clauses;
    }

    public String getTableName() {
        return table;
    }

    public SQLCommandResult execute() throws CommandExecutionException, SemanticError {
        Table table = getTable(getTableName());

        SemanticValidator validator = new SemanticValidator();
        validator.checkColumns(table, clauses.getColumns(), clauses.getValues());

        WhereMatcher matcher = new TableRecordMatcher(clauses);
        RecordSet result = getContext().getRecordManager().select(table, matcher);

        return new SQLCommandResult(new RecordSetCSVIterator(result, table));
    }

    private class RecordSetCSVIterator implements Iterator<String> {
        private RecordSet recordSet;
        private Table table;
        private boolean wasHeader = false;

        private RecordSetCSVIterator(RecordSet recordSet, Table table) {
            this.recordSet = recordSet;
            this.table = table;

            recordSet.iterator();
        }

        @Override
        public boolean hasNext() {
            return !wasHeader || recordSet.hasNext();

        }

        @Override
        public String next() {
            if (!wasHeader) {
                wasHeader = true;

                LinkedList<String> header = new LinkedList<>();

                for(int i = 0; i < table.getColumns().size(); i++) {
                    header.add(table.getColumns().get(i).getName());
                }

                return joinCSV(header);
            }

            TableRecord record = recordSet.nextMatched();
            while (record == null && hasNext()) {
                record = recordSet.nextMatched();
            }

            if (record == null) {
                return "";
            }

            LinkedList<String> row = new LinkedList<>();

            for(int i = 0; i < table.getColumns().size(); i++) {
                row.add(record.getValueAsString(table.getColumns().get(i).getName()));
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
