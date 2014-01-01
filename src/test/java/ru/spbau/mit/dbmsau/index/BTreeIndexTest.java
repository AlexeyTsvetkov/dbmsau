package ru.spbau.mit.dbmsau.index;

import org.junit.Before;
import org.junit.Test;
import ru.spbau.mit.dbmsau.relation.MemoryRelationRecord;
import ru.spbau.mit.dbmsau.table.Table;
import ru.spbau.mit.dbmsau.table.TestTableTest;

public class BTreeIndexTest extends TestTableTest {

    @Test
    public void testSelect() throws Exception {
        int from = 7, to = 10000;
        Table table = getTestTable();
        MemoryRelationRecord record = new MemoryRelationRecord(table);

        for (int i = from; i <= to; i++) {
            record.setValue(TEST_COLUMN_INDEX_ID, i);
            record.setValue(TEST_COLUMN_INDEX_NAME, "PREF_" + Integer.valueOf(i).toString());
            context.getTableRecordManager().insert(table, record);
        }

        IndexQuery indexQuery = new IndexQuery(TEST_COLUMN_INDEX_ID);

        BTreeIndex index = (BTreeIndex) context.
            getIndexManager().
            findAppropriateIndex(
                table,
                indexQuery
            );

        assertNotNull(index);


        indexQuery.setValue(TEST_COLUMN_INDEX_ID, 3);
        compareRecordSetTestContent(
            index.buildRecordSet(indexQuery),
            new String[][]{
                new String[]{
                    "3", "cde"
                }
            }
        );

        for (int i = from; i <= to; i++) {
            indexQuery.setValue(TEST_COLUMN_INDEX_ID, i);
            compareRecordSetTestContent(
                index.buildRecordSet(indexQuery),
                new String[][]{
                    new String[]{
                        Integer.valueOf(i).toString(), "PREF_" + Integer.valueOf(i).toString()
                    }
                }
            );
        }
    }

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        initSQLDumpLoad(TestTableTest.class, "create_insert_test.sql");
        executeCommands("CREATE INDEX test_index ON test(id)");
    }
}
