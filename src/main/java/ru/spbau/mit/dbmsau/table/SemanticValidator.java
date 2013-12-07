package ru.spbau.mit.dbmsau.table;

import ru.spbau.mit.dbmsau.table.exception.SemanticError;

import java.util.AbstractSet;
import java.util.HashSet;
import java.util.List;

public class SemanticValidator {
    private boolean checkType(Type type, String name, String value) {
        if (type.getType() == Type.TYPE_INTEGER) {
            try {
                int num = Integer.parseInt(value);
                return true;
            } catch (NumberFormatException e) {
                throw new SemanticError("`" + name + "` should be and integer");
            }
        }

        return true;
    }

    private void checkColumnExists(Table table, String column) {
        if (!table.hasColumn(column)) {
            String message = String.format("No such column `%s`", column);
            throw new SemanticError(message);
        }
    }

    private void checkColumnsUnique(List<String> columns) {
        AbstractSet<String> columnSet = new HashSet<>(columns.size());


        for(String column : columns) {
            if(columnSet.contains(column)) {
                String message = String.format("Column %s refered more than once", column);
                throw new SemanticError(message);
            }

            columnSet.add(column);
        }
    }

    public boolean checkColumns(Table table, List<String> columns, List<String> values)  {
        if (columns.size() != values.size()) {
            throw new SemanticError("Columns and values size are not equal");
        }

        for (int i = 0; i < columns.size(); i++) {

            String column = columns.get(i);
            String value  = values.get(i);

            checkColumnExists(table, column);

            Type type = table.getColumnType(column);
            checkType(type, column, value);
        }

        checkColumnsUnique(columns);

        return true;
    }
}
