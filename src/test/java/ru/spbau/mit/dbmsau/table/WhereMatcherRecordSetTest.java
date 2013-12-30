package ru.spbau.mit.dbmsau.table;

import org.junit.Test;
import ru.spbau.mit.dbmsau.BaseTest;
import ru.spbau.mit.dbmsau.relation.*;

import static org.hamcrest.CoreMatchers.is;

public class WhereMatcherRecordSetTest extends BaseTest {


    @Test
    public void testRecordSet() throws Exception {
        initSQLDumpLoad("create_test.sql");

        Table table = context.getTableManager().getTable("test");

        RelationRecord[] records = new RelationRecord[]{
            new MemoryRelationRecord(table, "1", "abc"),
            new MemoryRelationRecord(table, "2", "name"),
            new MemoryRelationRecord(table, "3", "name"),
            new MemoryRelationRecord(table, "1", "def"),
        };

        WhereMatcherRecordSet recordSet = new WhereMatcherRecordSet(
            new ArrayRecordSet(table, records),
            new StubMatcher()
        );

        recordSet.moveFirst();

        assertTrue(recordSet.hasNext());
        RelationRecord next = recordSet.next();
        assertThat(next.getValueAsString(TEST_COLUMN_INDEX_NAME), is("abc"));

        assertTrue(recordSet.hasNext());
        next = recordSet.next();
        assertThat(next.getValueAsString(TEST_COLUMN_INDEX_NAME), is("def"));

        assertFalse(recordSet.hasNext());
    }

    private class StubMatcher implements WhereMatcher {
        @Override
        public boolean matches(RelationRecord record) {
            return record.getValueAsString(TEST_COLUMN_INDEX_ID).equals("1");
        }

        @Override
        public void prepareFor(Relation relation) {

        }
    }
}
