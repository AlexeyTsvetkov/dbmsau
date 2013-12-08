package ru.spbau.mit.dbmsau.table;

public class RecordComparisonClause {
    private final String column;
    private final String sign;
    private final String rvalue;

    public RecordComparisonClause(String column, String sign, String rvalue) {
        this.column = column;
        this.sign = sign;
        this.rvalue = rvalue;
    }

    public String getColumn() {
        return column;
    }

    public String getSign() {
        return sign;
    }

    public String getRValue() {
        return rvalue;
    }
}
