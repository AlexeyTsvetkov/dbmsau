package ru.spbau.mit.dbmsau.command;

import ru.spbau.mit.dbmsau.command.exception.CommandExecutionException;
import ru.spbau.mit.dbmsau.table.Column;
import ru.spbau.mit.dbmsau.table.RecordSet;
import ru.spbau.mit.dbmsau.table.Table;
import ru.spbau.mit.dbmsau.table.TableRecord;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class SelectCommand extends AbstractSQLCommand {
    private String table;

    public SelectCommand(String table) {
        this.table = table;
    }

    public String getTableName() {
        return table;
    }

    public SQLCommandResult execute() throws CommandExecutionException {
        Table table = getTable(getTableName());
        RecordSet result = getContext().getRecordManager().select(table);

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

                for(int i = 0; i < table.getColumns().size(); i++) {
                    header.add(table.getColumns().get(i).getName());
                }

                return joinCSV(header);
            }

            TableRecord record = recordSet.next();

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
