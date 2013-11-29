package ru.spbau.mit.dbmsau.table;

import ru.spbau.mit.dbmsau.table.exception.SemanticError;

import java.util.List;

public class SemanticValidator {
    private boolean checkType(Type type, String name, String value) {
        if (type.getType() == Type.TYPE_INTEGER) {
            try {
                Integer.parseInt(value);
                return true;
            } catch (NumberFormatException e) {
                throw new SemanticError("`" + name + "` should be and integer");
            }
        }

        return true;
    }

    public boolean checkTypesCompatibility(Table table, List<String> columns,  List<String> values)  {
        if (columns.size() != values.size()) {
            throw new SemanticError("Columns and values size are not equal");
        }

        for (String column : columns) {
            if (!table.hasColumn(column)) {
                throw new SemanticError("No such column `" +  column + "`");
            }
        }

        for (int i = 0; i < columns.size(); i++) {
            int columnNumber = table.getColumnNumberByName(columns.get(i));
            Type type = table.getColumnTypeByNumber(columnNumber);
            checkType(type, columns.get(i), values.get(i));
        }

        return true;
    }
}
