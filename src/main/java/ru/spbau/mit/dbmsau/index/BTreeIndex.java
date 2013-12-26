package ru.spbau.mit.dbmsau.index;

import ru.spbau.mit.dbmsau.index.exception.IndexException;
import ru.spbau.mit.dbmsau.relation.RecordSet;
import ru.spbau.mit.dbmsau.table.Table;
import ru.spbau.mit.dbmsau.table.TableRecord;

import java.util.ArrayList;
import java.util.Arrays;

public class BTreeIndex extends Index {
    private int rootPageId;

    public BTreeIndex(String name, Table table, int[] columnIndexes, int rootPageId) {
        super(name, table, columnIndexes);
        this.rootPageId = rootPageId;
    }

    public int getRootPageId() {
        return rootPageId;
    }

    @Override
    public boolean isMatchingFor(int[] queryColumnIndexes, int matchingType) {
        //is there first column of index within the query
        return getInverseQueryColumnIndexes(queryColumnIndexes)[columnIndexes[0]] != -1;
    }

    public RecordSet buildRecordSetMatchingEqualityCondition(int[] queryColumnIndexes, String[] values) {
        if (queryColumnIndexes.length != values.length) {
            throw new IndexException("lengths of column numbers and values are not equal");
        }

        int[] inverseQueryColumnIndexes = getInverseQueryColumnIndexes(queryColumnIndexes);
        ArrayList<String> matchingValues = new ArrayList<>();

        for (int columnIndex : columnIndexes) {
            if (inverseQueryColumnIndexes[columnIndex] != -1) {
                matchingValues.add(values[inverseQueryColumnIndexes[columnIndex]]);
            }
        }

        return new BTReeRecordSet(matchingValues);
    }

    private int[] getInverseQueryColumnIndexes(int[] queryColumnIndexes) {
        int[] inverseQueryColumnIndexes = new int[table.getColumnsCount()];
        Arrays.fill(inverseQueryColumnIndexes, -1);

        for (int i = 0; i < queryColumnIndexes.length; i++) {
            inverseQueryColumnIndexes[queryColumnIndexes[i]] = i;
        }

        return inverseQueryColumnIndexes;
    }

    private class BTReeRecordSet extends RecordSet {
        ArrayList<String> matchingValues;

        private BTReeRecordSet(ArrayList<String> matchingValues) {
            super(null);
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
        public void moveFirst() {
            return;  //To change body of implemented methods use File | Settings | File Templates.
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
