package ru.spbau.mit.dbmsau.index;

import ru.spbau.mit.dbmsau.index.exception.IndexException;
import ru.spbau.mit.dbmsau.table.RecordSet;
import ru.spbau.mit.dbmsau.table.Table;
import ru.spbau.mit.dbmsau.table.TableRecord;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

public class BTreeIndex extends Index {
    private int rootPageId;

    public BTreeIndex(String name, Table table, int[] columnNumbers, int rootPageId) {
        super(name, table, columnNumbers);
        this.rootPageId = rootPageId;
    }

    public int getRootPageId() {
        return rootPageId;
    }

    @Override
    public boolean isMatchingFor(int[] queryColumnNumbers, int matchingType) {
        //is there first column of index within the query
        return getInverseQueryColumnNumbers(queryColumnNumbers)[columnNumbers[0]] != -1;
    }

    public RecordSet buildRecordSetMatchingEqualityCondition(int[] queryColumnNumbers, String[] values) {
        if (queryColumnNumbers.length != values.length) {
            throw new IndexException("lengths of column numbers and values are not equal");
        }

        int[] inverseQueryColumnNumbers = getInverseQueryColumnNumbers(queryColumnNumbers);
        ArrayList<String> matchingValues = new ArrayList<>();

        for (int columnNumber : columnNumbers) {
            if (inverseQueryColumnNumbers[columnNumber] != -1) {
                matchingValues.add(values[inverseQueryColumnNumbers[columnNumber]]);
            }
        }

        return new BTReeRecordSet(matchingValues);
    }

    private int[] getInverseQueryColumnNumbers(int[] queryColumnNumbers) {
        int[] inverseQueryColumnNumbers = new int[table.getColumns().size()];
        Arrays.fill(inverseQueryColumnNumbers, -1);

        for (int i = 0; i < queryColumnNumbers.length; i++) {
            inverseQueryColumnNumbers[queryColumnNumbers[i]] = i;
        }

        return inverseQueryColumnNumbers;
    }

    private class BTReeRecordSet extends RecordSet {
        ArrayList<String> matchingValues;

        private BTReeRecordSet(ArrayList<String> matchingValues) {
            this.matchingValues = matchingValues;
        }

        @Override
        public String toString() {
            String result = "";
            for (String s : matchingValues) {
                if (result.length() > 0) {
                    result += ", ";
                }
                result += s;
            }

            return result;
        }

        @Override
        public Iterator<TableRecord> iterator() {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public boolean hasNext() {
            return false;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public TableRecord next() {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void remove() {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    }
}
