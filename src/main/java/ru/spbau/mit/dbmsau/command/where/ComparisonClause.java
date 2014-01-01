package ru.spbau.mit.dbmsau.command.where;

import ru.spbau.mit.dbmsau.relation.*;
import ru.spbau.mit.dbmsau.table.SemanticValidator;

public class ComparisonClause implements WhereMatcher {
    private static final int SIGN_EQUALS = 0;
    private static final int SIGN_NOT_EQUALS = 1;
    private static final int SIGN_MORE_OR_EQUALS = 2;
    private static final int SIGN_LESS_EQUALS = 3;
    private static final int SIGN_MORE = 4;
    private static final int SIGN_LESS = 5;

    private ColumnAccessor accessor;
    private String value;

    private int signType;

    private int columnIndex;
    private boolean isInteger;
    private int intValue;

    public ComparisonClause(ColumnAccessor accessor, String value, String sign) {
        this.accessor = accessor;
        this.value = value;

        switch (sign) {
            case "=":
                signType = SIGN_EQUALS;
                break;
            case "<>":
                signType = SIGN_NOT_EQUALS;
                break;
            case ">":
                signType = SIGN_MORE;
                break;
            case "<":
                signType = SIGN_LESS;
                break;
            case ">=":
                signType = SIGN_MORE_OR_EQUALS;
                break;
            case "<=":
                signType = SIGN_LESS_EQUALS;
                break;
            default:
                throw new UnsupportedOperationException();
        }
    }

    public void prepareFor(Relation relation) {
        SemanticValidator.getInstance().checkColumnAccessor(relation, accessor);

        columnIndex = accessor.getColumnIndex(relation);

        SemanticValidator.getInstance().assertTypesCompatible(
            relation.getColumnType(columnIndex),
            accessor.toString(), value
        );

        isInteger = relation.getColumnType(columnIndex).isInteger();

        if (isInteger) {

            intValue = Integer.valueOf(value);
        }
    }

    @Override
    public boolean matches(RelationRecord record) {
        int cmpValue;

        if (isInteger) {
            cmpValue = record.getInteger(columnIndex) - intValue;
        } else {
            cmpValue = record.getString(columnIndex).compareTo(value);
        }

        switch (signType) {
            case SIGN_EQUALS:
                return cmpValue == 0;
            case SIGN_NOT_EQUALS:
                return cmpValue != 0;
            case SIGN_LESS:
                return cmpValue < 0;
            case SIGN_LESS_EQUALS:
                return cmpValue <= 0;
            case SIGN_MORE:
                return cmpValue > 0;
            case SIGN_MORE_OR_EQUALS:
                return cmpValue >= 0;
        }

        throw new UnsupportedOperationException();
    }

    public String getSignString() {
        switch (signType) {
            case SIGN_EQUALS:
                return "=";
            case SIGN_NOT_EQUALS:
                return "<>";
            case SIGN_LESS:
                return "<";
            case SIGN_LESS_EQUALS:
                return "<=";
            case SIGN_MORE:
                return ">";
            case SIGN_MORE_OR_EQUALS:
                return ">=";
        }

        return "#";
    }

    public String getValue() {
        return value;
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    public boolean isNotNotEquals() {
        return signType != SIGN_NOT_EQUALS;
    }

    public String toString() {
        return accessor.toString() + getSignString() + value;
    }
}
