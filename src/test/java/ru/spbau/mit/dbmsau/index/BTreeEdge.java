package ru.spbau.mit.dbmsau.index;

import org.junit.Before;
import org.junit.Test;
import ru.spbau.mit.dbmsau.Context;
import ru.spbau.mit.dbmsau.pages.FilePageManager;
import ru.spbau.mit.dbmsau.pages.PageManager;
import ru.spbau.mit.dbmsau.relation.*;
import ru.spbau.mit.dbmsau.table.*;

public class BTreeEdge extends TestTableTest {

    @Test
    public void testEdge() throws Exception {
        Table table = getTestTable();
//        //TableRecordsPage p = new TableRecordsPage(table, context.getPageManager().allocatePage());
//        TableRecord newRecord = p.getClearTableRecord();

        MemoryRelationRecord newRecord = new MemoryRelationRecord(table);
        final int n = 100000;
        for (int i = 1; i < n; i++) {
            newRecord.setValue(TEST_COLUMN_INDEX_ID, i);
            newRecord.setValue(TEST_COLUMN_INDEX_NAME, Integer.valueOf(i+1).toString());
            context.getTableRecordManager().insert(table, newRecord);
        }

        RecordSet solidRecordSet = new WhereMatcherRecordSet(
            context.getTableRecordManager().select(table),
            new WhereMatcher() {
                @Override
                public boolean matches(RelationRecord record) {
                    return record.getInteger(0) == n-1;
                }

                @Override
                public void prepareFor(Relation relation) {
                    //To change body of implemented methods use File | Settings | File Templates.
                }
            });

        compareRecordSetTestContent(
            solidRecordSet,
            new String[][] {
                new String[]{Integer.valueOf(n-1).toString(), Integer.valueOf(n).toString()}
            }
        );


        executeCommands("CREATE INDEX test_index on test (id)");
//
        IndexQuery indexQuery = new IndexQuery(TEST_COLUMN_INDEX_ID);
        BTreeIndex index = (BTreeIndex) context.getIndexManager().findAppropriateIndex(table, indexQuery);

//
//        for (int i = n-24; i < n; ++i) {
//            newRecord.setValue(TEST_COLUMN_INDEX_ID, i);
//            newRecord.setValue(TEST_COLUMN_INDEX_NAME, Integer.valueOf(i + 1).toString());
//            context.getTableRecordManager().insert(table, newRecord);
//        }
//
//        for (int i = 1; i<n-24; ++i) {
//            newRecord.setValue(TEST_COLUMN_INDEX_ID, i);
//            newRecord.setValue(TEST_COLUMN_INDEX_NAME, Integer.valueOf(i + 1).toString());
//            context.getTableRecordManager().insert(table, newRecord);
//        }

        RecordSet recordSet = index.buildRecordSetForJoin(n-1);

        compareRecordSetTestContent(
            recordSet,
            new String[][] {
                new String[]{Integer.valueOf(n-1).toString(), Integer.valueOf(n).toString()}
            }
        );
    }

    @Override
    protected PageManager buildPageManager(Context context) {
        return new FilePageManager(context);
    }

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        initSQLDumpLoad(TestTableTest.class, "create_test.sql");

    }
}
