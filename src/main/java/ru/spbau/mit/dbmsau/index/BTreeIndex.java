package ru.spbau.mit.dbmsau.index;

import ru.spbau.mit.dbmsau.Context;
import ru.spbau.mit.dbmsau.index.btree.BTree;
import ru.spbau.mit.dbmsau.index.btree.TreeTuple;
import ru.spbau.mit.dbmsau.relation.RecordSet;
import ru.spbau.mit.dbmsau.relation.RelationRecord;
import ru.spbau.mit.dbmsau.relation.Type;
import ru.spbau.mit.dbmsau.table.Table;
import ru.spbau.mit.dbmsau.table.TableRecord;
import ru.spbau.mit.dbmsau.table.TableRecordsPage;

import java.util.ArrayList;
import java.util.Arrays;

public class BTreeIndex extends Index {
    private int rootPageId;
    private BTree btree;
    private Context context;

    public BTreeIndex(String name, Table table, int[] columnIndexes, int rootPageId, Context context) {
        super(name, table, columnIndexes);
        this.rootPageId = rootPageId;
        this.context = context;

        // Construct BTree
        Type[] valueTypes = new Type[]{Type.getIntegerType(), Type.getIntegerType()};
        Type[] keyTypes = new Type[columnIndexes.length];

        for(int i=0; i < columnIndexes.length; ++i){
            keyTypes[i] = table.getColumnType(columnIndexes[i]);
        }

        btree = new BTree(keyTypes, valueTypes, rootPageId, context);
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
    public RecordSet buildRecordSetForJoin(int onValue) {
        return new BTReeRecordSet(new IndexQueryRange(onValue, onValue));
    }

    @Override
    public void processNewRecord(TableRecord record) {
        TreeTuple key = btree.getNewKeyTuple(columnIndexes, record);
        TreeTuple val = btree.getNewValTuple(record);

        btree.put(key, val);
    }

    @Override
    public boolean isDuplicateViolation(RelationRecord record) {
        TreeTuple key = btree.getNewKeyTuple(columnIndexes, record);
        return btree.containsKey(key);
    }

    @Override
    public void processDeletedRecord(TableRecord record) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void initFirstTime() {
        super.initFirstTime();
        btree.initFirstTime();
    }

    private class BTReeRecordSet extends RecordSet {
        IndexQueryRange[] ranges;
        private TableRecordsPage currentRecordPage;
        private TreeTuple currentVal;

        private BTReeRecordSet(ArrayList<IndexQueryRange> ranges) {
            super(table);
            this.ranges = ranges.toArray(new IndexQueryRange[ranges.size()]);
        }

        private BTReeRecordSet(IndexQueryRange... ranges) {
            super(table);
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

        private TableRecord getTableRecord(int pageId, int slotIndex) {
            currentRecordPage = new TableRecordsPage(
                table,
                context.getPageManager().getPageById(pageId, false)
            );

            return currentRecordPage.getTableRecordFromSlot(slotIndex);
        }

        @Override
        public void moveFirst() {
            currentVal = btree.get(TreeTuple.getOneIntTuple(ranges[0].getFrom()));
        }

        @Override
        public boolean hasNext() {
            return currentVal!=null;
        }

        @Override
        public TableRecord next() {
            TableRecord res = getTableRecord(currentVal.getInteger(0), currentVal.getInteger(4));
            currentVal = null;
            return res;
        }

        @Override
        public void remove() {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    }
}
