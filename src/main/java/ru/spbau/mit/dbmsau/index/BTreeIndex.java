package ru.spbau.mit.dbmsau.index;

import ru.spbau.mit.dbmsau.relation.RecordSet;
import ru.spbau.mit.dbmsau.relation.RelationRecord;
import ru.spbau.mit.dbmsau.table.Table;
import ru.spbau.mit.dbmsau.table.TableRecord;

import java.util.ArrayList;

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
    public boolean isMatchingFor(IndexQuery query) {
        //is there first column of index within the query
        return query.isThereColumn(columnIndexes[0]);
    }

    public RecordSet buildRecordSet(IndexQuery query) {
        ArrayList<IndexQueryRange> matchingRanges = new ArrayList<>();

        for (int columnIndex : columnIndexes) {
            if (!query.isThereColumn(columnIndex)) {
                break;
            }

            matchingRanges.add(query.getRange(columnIndex));
        }

        return new BTReeRecordSet(matchingRanges);
    }

    @Override
    public void processNewRecord(TableRecord record) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isDuplicateViolation(RelationRecord record) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void processDeletedRecord(TableRecord record) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void initFirstTime() {
        super.initFirstTime();

    }

    private class BTReeRecordSet extends RecordSet {
        ArrayList<IndexQueryRange> ranges;

        private BTReeRecordSet(ArrayList<IndexQueryRange> ranges) {
            super(null);
            this.ranges = ranges;
        }

        @Override
        public String toString() {
            String result = "";
            for (IndexQueryRange s : ranges) {
                if (result.length() > 0) {
                    result += ", ";
                }
                result += s.toString();
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
