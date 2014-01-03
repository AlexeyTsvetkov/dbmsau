package ru.spbau.mit.dbmsau.index;

import org.junit.Before;
import org.junit.Test;
import ru.spbau.mit.dbmsau.relation.MemoryRelationRecord;
import ru.spbau.mit.dbmsau.relation.RecordSet;
import ru.spbau.mit.dbmsau.table.Table;
import ru.spbau.mit.dbmsau.table.TestTableTest;

public class BTreeIndexTest extends TestTableTest {
    private String[] getShouldBeRow(int id) {
        if (id <= 5) {
            return defaultContent[id / 2];
        }

        return new String[]{
            Integer.valueOf(id).toString(),
            "PREF_" + Integer.valueOf(id).toString()
        };
    }

    private void defaultDataInsert(int from, int to) throws Exception {
        Table table = getTestTable();
        MemoryRelationRecord record = new MemoryRelationRecord(table);

        for (int i = from; i <= to; i++) {
            record.setValue(TEST_COLUMN_INDEX_ID, i);
            record.setValue(TEST_COLUMN_INDEX_NAME, getShouldBeRow(i)[1]);
            context.getTableRecordManager().insert(table, record);
        }
    }

    @Test
    public void testSelect() throws Exception {
        int from = 7, to = 10000;
        Table table = getTestTable();
        defaultDataInsert(from, to);

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
                    getShouldBeRow(i)
                }
            );
        }
    }

    private void removeRecordSetContent(RecordSet recordSet) {
        recordSet.moveFirst();

        while (recordSet.hasNext()) {
            recordSet.next();
            recordSet.remove();
        }
    }

    @Test
    public void testDelete() throws Exception {
        int from = 7, to = 10000;
        Table table = getTestTable();

        defaultDataInsert(from, to);

        IndexQuery indexQuery = new IndexQuery(TEST_COLUMN_INDEX_ID);

        BTreeIndex index = (BTreeIndex) context.
            getIndexManager().
            findAppropriateIndex(
                table,
                indexQuery
            );

        assertNotNull(index);

        indexQuery.setValue(TEST_COLUMN_INDEX_ID, 3);

        removeRecordSetContent(
            index.buildRecordSet(indexQuery)
        );

        String[][] shouldBe = new String[to - from + 4 - 1][];
        shouldBe[0] = getShouldBeRow(1);
        shouldBe[1] = getShouldBeRow(5);
        int cnt = 2;

        for (int i = from; i <= to; i++) {
            shouldBe[cnt++] = getShouldBeRow(i);
        }

        compareTestContent(shouldBe);

        indexQuery = new IndexQuery(
            new int[]{TEST_COLUMN_INDEX_ID, TEST_COLUMN_INDEX_ID},
            new String[]{">=", "<="},
            new String[]{"5", "3000"}
        );

        removeRecordSetContent(
            index.buildRecordSet(indexQuery)
        );

        shouldBe = new String[to - from + 4 - (3000 - from + 3)][];

        shouldBe[0] = getShouldBeRow(1);
        cnt = 1;

        for (int i = 3001; i <= to; i++) {
            shouldBe[cnt++] = getShouldBeRow(i);
        }

        assert cnt == shouldBe.length;

        compareTestContent(shouldBe);

        indexQuery = new IndexQuery(
            new int[]{TEST_COLUMN_INDEX_ID, TEST_COLUMN_INDEX_ID},
            new String[]{">=", "<="},
            new String[]{"1", Integer.valueOf(to).toString()}
        );

        compareRecordSetTestContent(
            index.buildRecordSet(indexQuery),
            shouldBe
        );
    }

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        initSQLDumpLoad(TestTableTest.class, "create_insert_test.sql");
        executeCommands("CREATE INDEX test_index ON test(id)");
    }
}
