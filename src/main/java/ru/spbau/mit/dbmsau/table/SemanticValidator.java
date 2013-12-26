package ru.spbau.mit.dbmsau.table;

import ru.spbau.mit.dbmsau.relation.Type;
import ru.spbau.mit.dbmsau.table.exception.SemanticError;

import java.util.AbstractSet;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class SemanticValidator {
    private void assertTypesCompatible(Type type, String name, String value) {
        if (type.getType() == Type.TYPE_INTEGER) {

            try {
                Integer.parseInt(value);
            } catch (NumberFormatException e) {
                String message = String.format("`%s` should be an integer", name);
                throw new SemanticError(message);
            }
        }
    }

    private void assertColumnExists(Table table, String column) {
        if (!table.hasColumn(column)) {
            String message = String.format("No such column `%s`", column);
            throw new SemanticError(message);
        }
    }

    public void assertColumnsUnique(List<String> columns) {
        AbstractSet<String> columnSet = new HashSet<>(columns.size());


        for (String column : columns) {
            if (columnSet.contains(column)) {
                String message = String.format("Column `%s` referenced more than once", column);
                throw new SemanticError(message);
            }

            columnSet.add(column);
        }
    }

    public void checkCreateTable(Table table, TableManager tableManager) throws SemanticError {
        String name = table.getName();
        if (tableManager.tableExists(name)) {
            String message = String.format("Table `%s` already exists", name);
            throw new SemanticError(message);
        }

        List<String> columns = new LinkedList<>();

        for (int i = 0; i < table.getColumnsCount(); i++) {
            columns.add(table.getColumnName(i));
        }

        assertColumnsUnique(columns);
    }

    public boolean checkColumns(Table table, List<String> columns, List<String> values) {
        if (columns.size() != values.size()) {
            throw new SemanticError("Columns and values size are not equal");
        }

        for (int i = 0; i < columns.size(); i++) {

            String column = columns.get(i);
            String value = values.get(i);

            assertColumnExists(table, column);

            Type type = table.getColumnType(table.getColumnIndex(column));
            assertTypesCompatible(type, column, value);
        }

        assertColumnsUnique(columns);

        return true;
    }
}
