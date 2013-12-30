package ru.spbau.mit.dbmsau.index;

import ru.spbau.mit.dbmsau.Context;
import ru.spbau.mit.dbmsau.ContextContainer;
import ru.spbau.mit.dbmsau.index.exception.DuplicateKeyException;
import ru.spbau.mit.dbmsau.relation.RecordSet;
import ru.spbau.mit.dbmsau.relation.RelationRecord;
import ru.spbau.mit.dbmsau.table.Table;
import ru.spbau.mit.dbmsau.table.TableRecord;

import java.util.List;

abstract public class IndexManager extends ContextContainer {

    public IndexManager(Context context) {
        super(context);
    }

    public void init() {

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

    abstract public void createIndex(String name, Table table, List<String> columns);

    abstract public Iterable<Index> getIndexesForTable(Table table);
}
