package ru.spbau.mit.dbmsau.relation;

import java.util.ArrayList;

public class StubRelation extends Relation {
    private static ArrayList<Column> buildColumns(String tableName, String... columnNames) {
        ArrayList<Column> columns = new ArrayList<>();

        for (String columnName : columnNames) {
            columns.add(new Column(tableName, columnName, Type.getIntegerType()));
        }

        return columns;
    }

    public StubRelation(String tableName, String... columnNames) {
        super(buildColumns(tableName, columnNames));
    }
}
