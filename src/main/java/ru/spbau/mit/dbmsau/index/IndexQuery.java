package ru.spbau.mit.dbmsau.index;

import org.apache.commons.lang3.math.NumberUtils;
import ru.spbau.mit.dbmsau.index.exception.IndexException;

import java.util.ArrayList;

public class IndexQuery {
    ArrayList<Integer>[] matchingTypes;
    ArrayList<Integer>[] values;

    public IndexQuery(int[] queryColumnIndexes, String[] ops, String[] values) {
        assert queryColumnIndexes.length == values.length;
        assert ops.length == values.length;

        int maxQueryColumnIndex = NumberUtils.max(queryColumnIndexes);

        init(maxQueryColumnIndex);

        for (int i = 0; i < queryColumnIndexes.length; i++) {
            this.matchingTypes[queryColumnIndexes[i]].add(getMatchingTypeForOperator(ops[i]));
            int value = 0;

            try {
                value = Integer.valueOf(values[i]);
            } catch (NumberFormatException e) {

            }

            this.values[queryColumnIndexes[i]].add(value);

        }
    }

    public IndexQuery(int columnIndex, String op) {
        init(columnIndex);
        matchingTypes[columnIndex].add(getMatchingTypeForOperator(op));
        values[columnIndex].add(0);
    }

    public IndexQuery(int columnIndex) {
        this(columnIndex, "=");
    }

    private void init(int maxQueryColumnIndex) {
        maxQueryColumnIndex += 1;
        this.matchingTypes = new ArrayList[maxQueryColumnIndex];
        this.values = new ArrayList[maxQueryColumnIndex];

        for (int i = 0; i < maxQueryColumnIndex; i++) {
            this.matchingTypes[i] = new ArrayList<>();
            this.values[i] = new ArrayList<>();
        }
    }

    public boolean isThereColumn(int columnIndex) {
        return columnIndex < matchingTypes.length && matchingTypes[columnIndex].size() > 0;
    }

    public IndexQueryRange getRange(int columnIndex) {
        if (!isThereColumn(columnIndex)) {
            return null;
        }

        int from = -1, to = -1;

        for (int i = 0; i < matchingTypes[columnIndex].size(); i++) {
            int value = values[columnIndex].get(i);
            int type = matchingTypes[columnIndex].get(i);

            if (type == Index.MATCHING_TYPE_EQUALITY) {
                return new IndexQueryRange(values[columnIndex].get(i), value);
            }

            if (type == Index.MATCHING_TYPE_GREATER) {
                from = value + 1;
            }
            if (type == Index.MATCHING_TYPE_GREATER_OR_EQUAL) {
                from = value;
            }

            if (type == Index.MATCHING_TYPE_LESS) {
                to = value - 1;
            }
            if (type == Index.MATCHING_TYPE_LESS_OR_EQUAL) {
                to = value;
            }
        }

        return new IndexQueryRange(from, to);
    }

    public void setValue(int columnIndex, int value) {
        values[columnIndex].set(0, value);
    }

    private int getMatchingTypeForOperator(String op) {
        switch (op) {
            case "=":
                return Index.MATCHING_TYPE_EQUALITY;
            case ">":
                return Index.MATCHING_TYPE_GREATER;
            case ">=":
                return Index.MATCHING_TYPE_GREATER_OR_EQUAL;
            case "<":
                return Index.MATCHING_TYPE_LESS;
            case "<=":
                return Index.MATCHING_TYPE_LESS_OR_EQUAL;

            default:
                throw new IndexException("Unknown operator: " + op);
        }
    }
}
