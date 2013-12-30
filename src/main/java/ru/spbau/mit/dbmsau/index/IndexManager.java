package ru.spbau.mit.dbmsau.index;

import ru.spbau.mit.dbmsau.Context;
import ru.spbau.mit.dbmsau.ContextContainer;
import ru.spbau.mit.dbmsau.index.exception.DuplicateKeyException;
import ru.spbau.mit.dbmsau.index.exception.IndexCreationError;
import ru.spbau.mit.dbmsau.index.exception.IndexException;
import ru.spbau.mit.dbmsau.index.exception.IndexManagerException;
import ru.spbau.mit.dbmsau.relation.RecordSet;
import ru.spbau.mit.dbmsau.relation.RelationRecord;
import ru.spbau.mit.dbmsau.table.Table;
import ru.spbau.mit.dbmsau.table.TableRecord;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

abstract public class IndexManager extends ContextContainer {
    private HashMap<String, ArrayList<Index>> indexesByTables = new HashMap<>();
    private HashMap<String, Index> indexesByNames = new HashMap<>();

    public IndexManager(Context context) {
        super(context);
    }

    public void init() {

    }

    public void createIndex(String name, Table table, List<String> columns) {
        int[] columnIndexes = table.getColumnIndexesByNames(columns);

        if (indexesByNames.containsKey(name)) {
            throw new IndexCreationError("index `" + name + "` already exists");
        }

        Index index = buildIndex(name, table, columnIndexes);

        try {
            saveIndex(index);
        } catch (IndexException e) {
            throw new IndexManagerException("Can't save index");
        }

        addIndex(index);
    }

    protected Index buildIndex(String name, Table table, int[] columnIndexes) {
        //for BTree only
        int rootPageId = context.getPageManager().doAllocatePage();
        return new BTreeIndex(name, table, columnIndexes, rootPageId);
    }

    public Iterable<Index> getIndexesForTable(Table table) {
        Iterable<Index> result = indexesByTables.get(table.getName());

        if (result == null) {
            result = Collections.emptyList();
        }

        return result;
    }

    protected void addIndex(Index index) {
        String tableName = index.getTable().getName();

        if (!indexesByTables.containsKey(tableName)) {
            indexesByTables.put(tableName, new ArrayList<Index>());
        }

        indexesByTables.get(tableName).add(index);
        indexesByNames.put(index.getName(), index);
    }

    public Index findAppropriateIndex(Table table, int[] columnsIndexes) {
        for (Index index : getIndexesForTable(table)) {
            if (index.isMatchingFor(columnsIndexes, Index.EQUALITY_MATCHING_TYPE)) {
                return index;
            }
        }

        return null;
    }

    public RecordSet buildIndexedRecordSetIfPossible(Table table, int[] candidatesColumnsIndexes, String[] values) {
        Index index = findAppropriateIndex(table, candidatesColumnsIndexes);

        if (index != null) {
            return index.buildRecordSetMatchingEqualityCondition(candidatesColumnsIndexes, values);
        }

        return null;
    }

    public boolean checkConstraintsBeforeInsert(Table table, RelationRecord record) throws DuplicateKeyException {
        for (Index index : context.getIndexManager().getIndexesForTable(table)) {
            if (index.isDuplicateViolation(record)) {
                throw new DuplicateKeyException("Duplicate entry for index `" + index.getName() + "`");
            }
        }

        return true;
    }

    public void processNewRecord(Table table, TableRecord record) {
        for (Index index : context.getIndexManager().getIndexesForTable(table)) {
            index.processNewRecord(record);
        }
    }

    public void processDeletedRecord(Table table, TableRecord record) {
        for (Index index : context.getIndexManager().getIndexesForTable(table)) {
            index.processDeletedRecord(record);
        }
    }

    abstract protected void saveIndex(Index index) throws IndexException;
}
