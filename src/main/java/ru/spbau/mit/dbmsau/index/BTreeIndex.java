package ru.spbau.mit.dbmsau.index;

import ru.spbau.mit.dbmsau.Context;
import ru.spbau.mit.dbmsau.index.btree.BTree;
import ru.spbau.mit.dbmsau.index.btree.Node;
import ru.spbau.mit.dbmsau.index.btree.NodeData;
import ru.spbau.mit.dbmsau.index.btree.TreeTuple;
import ru.spbau.mit.dbmsau.relation.RecordSet;
import ru.spbau.mit.dbmsau.relation.RelationRecord;
import ru.spbau.mit.dbmsau.relation.Type;
import ru.spbau.mit.dbmsau.table.Table;
import ru.spbau.mit.dbmsau.table.TableRecord;
import ru.spbau.mit.dbmsau.table.TableRecordsPage;

import java.util.ArrayList;

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

        for (int i = 0; i < columnIndexes.length; ++i) {
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
        TreeTuple key = btree.getNewKeyTuple(columnIndexes, record);
        btree.remove(key);
    }

    @Override
    public void initFirstTime() {
        super.initFirstTime();
        btree.initFirstTime();
    }

    private class BTReeRecordSet extends RecordSet {
        IndexQueryRange[] ranges;
        private TableRecordsPage currentRecordPage;
        private Node currentNode;
        private BTree.ItemLocation currentLoc;
        private TreeTuple leftBound, rightBound;

        private TableRecord currentTableRecord;

        private BTReeRecordSet(ArrayList<IndexQueryRange> ranges) {
            super(table);
            this.ranges = ranges.toArray(new IndexQueryRange[ranges.size()]);
            init();
        }

        private BTReeRecordSet(IndexQueryRange... ranges) {
            super(table);
            this.ranges = ranges;
            init();
        }

        void init() {
            Object[] leftBound = new Object[columnIndexes.length];
            Object[] rightBound = new Object[columnIndexes.length];

            for (int i = 0; i < ranges.length; ++i) {
                leftBound[i] = ranges[i].getFrom();
                rightBound[i] = ranges[i].getTo();
            }

            for (int i = ranges.length; i < columnIndexes.length; ++i) {
                leftBound[i] = Integer.MIN_VALUE;
                rightBound[i] = Integer.MAX_VALUE;
            }

            this.leftBound = btree.getNewKeyTuple(leftBound);
            this.rightBound = btree.getNewKeyTuple(rightBound);
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
            currentLoc = btree.lower_bound(leftBound);
            currentNode = btree.getNodeById(currentLoc.getNodeId(), false);
        }

        @Override
        public boolean hasNext() {
            moveToKey();
            return currentLoc != null && btree.cmp(btree.getLowerBoundKey(currentLoc), rightBound) <= 0;
        }

        @Override
        public TableRecord next() {
            currentTableRecord = null;

            moveToKey();
            TreeTuple val = currentNode.getNodeData().getValue(currentLoc.getIndex());
            currentLoc.setIndex(currentLoc.getIndex() + 1);

            return currentTableRecord = getTableRecord(val.getInteger(0), val.getInteger(4));
        }

        @Override
        public void remove() {
            for (Index index : context.getIndexManager().getIndexesForTable(table)) {
                index.processDeletedRecord(currentTableRecord);
            }
            currentLoc.setIndex(currentLoc.getIndex() - 1);
            currentRecordPage.freeRecord(currentTableRecord.getRecord().getSlotIndex());
        }

        private void moveToKey() {
            if (currentLoc == null)
                return;

            while (currentLoc.getIndex() >= currentNode.getNodeData().getAmountOfKeys() &&
                    currentNode.getNodeData().getNextNodeId() != NodeData.NO_NODE_ID) {
                currentNode = btree.getNodeById(currentNode.getNodeData().getNextNodeId(), false);
                currentLoc.setNodeId(currentNode.getNodeId());
                currentLoc.setIndex(0);
            }

            if (currentLoc.getIndex() >= currentNode.getNodeData().getAmountOfKeys()) {
                currentLoc = null;
            }
        }
    }
}
