package ru.spbau.mit.dbmsau.table;

import ru.spbau.mit.dbmsau.Context;
import ru.spbau.mit.dbmsau.relation.ArrayRecordSet;
import ru.spbau.mit.dbmsau.relation.RecordSet;
import ru.spbau.mit.dbmsau.relation.StubRelationRecord;

public class StubTableRecordManager extends TableRecordManager {
    public StubTableRecordManager(Context context) {
        super(context);
    }

    @Override
    public RecordSet select(Table table) {
        return new ArrayRecordSet(
            table,
            new StubRelationRecord(table, "1", "asd"),
            new StubRelationRecord(table, "2", "def")
        );
    }
}
