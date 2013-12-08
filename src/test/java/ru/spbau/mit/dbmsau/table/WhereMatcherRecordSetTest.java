package ru.spbau.mit.dbmsau.table;

import static org.hamcrest.CoreMatchers.*;
import org.junit.Test;
import ru.spbau.mit.dbmsau.BaseTest;
import ru.spbau.mit.dbmsau.syntax.StubTableRecord;

public class WhereMatcherRecordSetTest extends BaseTest {


    @Test
    public void testRecordSet() throws Exception {
        initSQLDumpLoad("create_test.sql");

        Table table = context.getTableManager().getTable("test");

        TableRecord[] records = new TableRecord[]{
            new StubTableRecord(table, new String[]{"1", "abc"}),
            new StubTableRecord(table, new String[]{"2", "name"}),
            new StubTableRecord(table, new String[]{"3", "name"}),
            new StubTableRecord(table, new String[]{"1", "def"}),
        };

        WhereMatcherRecordSet recordSet = new WhereMatcherRecordSet(
                new ArrayRecordSet(records),
                new StubMatcher()
        );

        recordSet.iterator();

        assertTrue(recordSet.hasNext());
        TableRecord next = recordSet.next();
        assertThat(next.getValueAsString("name"), is("abc"));

        assertTrue(recordSet.hasNext());
        next = recordSet.next();
        assertThat(next.getValueAsString("name"), is("def"));

        assertFalse(recordSet.hasNext());
    }

    private class StubMatcher implements WhereMatcher {
        @Override
        public boolean matches(TableRecord record) {
            return record.getValueAsString("id").equals("1");
        }
    }
}
