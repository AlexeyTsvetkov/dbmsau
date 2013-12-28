package ru.spbau.mit.dbmsau.table;

import ru.spbau.mit.dbmsau.Context;
import ru.spbau.mit.dbmsau.relation.RecordSet;
import ru.spbau.mit.dbmsau.relation.StubRelationRecord;
import ru.spbau.mit.dbmsau.relation.WhereMatcher;

public class StubTableRecordManager extends TableRecordManager {
    public StubTableRecordManager(Context context) {
        super(context);
    }

    @Override
    public RecordSet select(Table table, WhereMatcher matcher) {
        return new ArrayRecordSet(
            table,
            new StubRelationRecord(table, "1", "asd"),
            new StubRelationRecord(table, "2", "def")
        );
    }
}
