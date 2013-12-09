package ru.spbau.mit.dbmsau.table;

public class RecordComparisonClause {
    private final String column;
    private final String sign;
    private final String value;

    public RecordComparisonClause(String column, String sign, String value) {
        this.column = column;
        this.sign = sign;
        this.value = value;
    }

    public String getColumn() {
        return column;
    }

    public String getSign() {
        return sign;
    }

    public String getValue() {
        return value;
    }
}
