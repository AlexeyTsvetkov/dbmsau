package ru.spbau.mit.dbmsau.table;

import ru.spbau.mit.dbmsau.Context;
import ru.spbau.mit.dbmsau.relation.ArrayRecordSet;
import ru.spbau.mit.dbmsau.relation.MemoryRelationRecord;
import ru.spbau.mit.dbmsau.relation.RecordSet;

public class StubTableRecordManager extends TableRecordManager {
    public StubTableRecordManager(Context context) {
        super(context);
    }

    @Override
    public RecordSet select(Table table) {
        return new ArrayRecordSet(
            table,
            new MemoryRelationRecord(table, "1", "asd"),
            new MemoryRelationRecord(table, "2", "def")
        );
    }
}
