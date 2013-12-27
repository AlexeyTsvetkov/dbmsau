package ru.spbau.mit.dbmsau.table;

import org.junit.Test;
import ru.spbau.mit.dbmsau.BaseTest;
import ru.spbau.mit.dbmsau.relation.RelationRecord;
import ru.spbau.mit.dbmsau.relation.StubRelationRecord;
import ru.spbau.mit.dbmsau.relation.WhereMatcher;
import ru.spbau.mit.dbmsau.relation.WhereMatcherRecordSet;

import static org.hamcrest.CoreMatchers.is;

public class WhereMatcherRecordSetTest extends BaseTest {


    @Test
    public void testRecordSet() throws Exception {
        initSQLDumpLoad("create_test.sql");

        Table table = context.getTableManager().getTable("test");

        RelationRecord[] records = new RelationRecord[]{
            new StubRelationRecord(table, new String[]{"1", "abc"}),
            new StubRelationRecord(table, new String[]{"2", "name"}),
            new StubRelationRecord(table, new String[]{"3", "name"}),
            new StubRelationRecord(table, new String[]{"1", "def"}),
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
    }
}
