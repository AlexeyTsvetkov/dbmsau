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

        int[][] shouldBe = new int[][]{
                {1,2}, {3,4}, {5,6}
        };

        compareTestContent(shouldBe);
    }

    @Test
    public void testSelectBig() throws Exception {
        initSQLDumpLoad("create_test.sql");

        int[][] shouldBe = new int[5000][2];

        for (int i = 0; i < shouldBe.length; i++) {
            shouldBe[i][0] = i;
            shouldBe[i][1] = -i;

            ArrayList<String> columns = new ArrayList<>();
            ArrayList<String> values = new ArrayList<>();

            columns.add("id");values.add(Integer.valueOf(shouldBe[i][0]).toString());
            columns.add("name");values.add(Integer.valueOf(shouldBe[i][1]).toString());

            context.getRecordManager().insert(context.getTableManager().getTable("test"), columns, values);
        }

        compareTestContent(shouldBe);
    }

    private void compareTestContent(int[][] shouldBe) {

        Iterator<TableRecord> recordSet = context.getRecordManager().select(context.getTableManager().getTable("test")).iterator();

        for (int[] a: shouldBe) {
            assertTrue(recordSet.hasNext());

            TableRecord next = recordSet.next();

            assertThat(next.getIntegerValue("id"), is(Integer.valueOf(a[0]).toString()));
            assertThat(next.getIntegerValue("name"), is(Integer.valueOf(a[1]).toString()));
        }

        assertFalse(recordSet.hasNext());
    }
}
