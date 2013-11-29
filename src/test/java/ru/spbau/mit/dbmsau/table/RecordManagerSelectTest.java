package ru.spbau.mit.dbmsau.table;

import static org.hamcrest.CoreMatchers.*;
import org.junit.Test;
import ru.spbau.mit.dbmsau.BaseTest;

import java.util.ArrayList;
import java.util.Iterator;

public class RecordManagerSelectTest extends BaseTest {
    @Test
    public void testSelect() throws Exception {
        initSQLDumpLoad("create_insert_test.sql");

        String[][] shouldBe = new String[][]{
                {"1","abc"}, {"3","cde"}, {"5","efg"}
        };

        compareTestContent(shouldBe);
        compareTestContent(shouldBe);
    }

    @Test
    public void testSelectBig() throws Exception {
        initSQLDumpLoad("create_test.sql");

        String[][] shouldBe = new String[5000][2];

        for (int i = 0; i < shouldBe.length; i++) {
            shouldBe[i][0] = Integer.valueOf(i).toString();
            shouldBe[i][1] = Integer.valueOf(-i).toString();

            ArrayList<String> columns = new ArrayList<>();
            ArrayList<String> values = new ArrayList<>();

            columns.add("id");values.add(shouldBe[i][0]);
            columns.add("name");values.add(shouldBe[i][1]);

            context.getRecordManager().insert(context.getTableManager().getTable("test"), columns, values);
        }

        compareTestContent(shouldBe);
        compareTestContent(shouldBe);
    }

    private void compareTestContent(String[][] shouldBe) {

        Iterator<TableRecord> recordSet = context.getRecordManager().select(context.getTableManager().getTable("test")).iterator();

        for (String[] a: shouldBe) {
            assertTrue(recordSet.hasNext());

            TableRecord next = recordSet.next();

            assertThat(next.getValueAsString("id"), is(a[0]));
            assertThat(next.getValueAsString("name"), is(a[1]));
        }

        assertFalse(recordSet.hasNext());

        checkBusyPages();
    }
}
