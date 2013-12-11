package ru.spbau.mit.dbmsau.command;

import ru.spbau.mit.dbmsau.table.*;

import java.util.Iterator;

public class TableRecordMatcher implements WhereMatcher {

    private ClauseIterator clauses;

    TableRecordMatcher(ClauseIterator clauses) {
        this.clauses = clauses;
    }

    private boolean intMatches(String recordValue, String compareValue, String sign) {
        int recordVal  = Integer.valueOf(recordValue);
        int compareVal = Integer.valueOf(compareValue);

        if (sign.equals("<") && recordVal >= compareVal) {
            return false;
        }

        if (sign.equals("=") && recordVal != compareVal) {
            return false;
        }

        if (sign.equals(">") && recordVal <= compareVal) {
            return false;
        }

        return true;
    }

    private boolean stringMatches(String recordValue, String compareValue, String sign) {
        int compareResult = recordValue.compareTo(compareValue);
        if (sign.equals("<") && compareResult >= 0) {
            return false;
        }

        if (sign.equals("=") && compareResult != 0) {
            return false;
        }

        if (sign.equals(">") && compareResult <= 0) {
            return false;
        }

        return true;
    }

    @Override
    public boolean matches(TableRecord record) {
        Iterator<RecordClause> iterator = clauses.iterator();
        while (iterator.hasNext()) {
            RecordClause clause = iterator.next();

            String column        = clause.getColumn();
            String sign          = clause.getSign();
            String compareValue  = clause.getValue();
            String recordValue = record.getValueAsString(column);

            int type = record.getColumnType(column).getType();

            if (type == Type.TYPE_INTEGER && !intMatches(recordValue, compareValue, sign)) {
                return false;
            }

            if (type == Type.TYPE_VARCHAR && !stringMatches(recordValue, compareValue, sign)) {
                return false;
            }
        }

        return true;
    }
}
