package ru.spbau.mit.dbmsau.table;

import org.junit.Test;
import ru.spbau.mit.dbmsau.relation.RecordSet;

import java.util.ArrayList;


public class RecordSetTest extends TestTableTest {
    @Test
    public void testIteratorRemove() throws Exception {
        initSQLDumpLoad("create_insert_test.sql");

        RecordSet recordSet = context.getTableRecordManager().select(context.getTableManager().getTable("test"));

        recordSet.moveFirst();
        recordSet.next();
        recordSet.next();
        recordSet.remove();

        String[][] shouldBe = new String[][]{
                {"1", "abc"}, {"5", "efg"}
        };

        compareTestContent(shouldBe);
    }

    @Test
    public void testIteratorRemoveBig() throws Exception {
        initSQLDumpLoad("create_test.sql");
        int n = 6000;
        int cutFrom = 3000, cutTo = 5500;

        String[][] shouldBe = new String[n][2];

        for (int i = 0; i < n; i++) {
            shouldBe[i][0] = Integer.valueOf(i).toString();
            shouldBe[i][1] = Integer.valueOf(-i).toString();

            ArrayList<String> columns = new ArrayList<>();
            ArrayList<String> values = new ArrayList<>();

            columns.add("id");
            values.add(shouldBe[i][0]);
            columns.add("name");
            values.add(shouldBe[i][1]);

            context.getTableRecordManager().insert(context.getTableManager().getTable("test"), columns, values);
        }

        RecordSet recordSet = context.getTableRecordManager().select(context.getTableManager().getTable("test"));
        recordSet.moveFirst();

        String[][] newShouldBe = new String[n - (cutTo - cutFrom + 1)][2];
        int counter = 0;

        for (int i = 0; i < n; i++) {
            assertTrue(recordSet.hasNext());

            recordSet.next();

            if (i >= cutFrom && i <= cutTo) {
                recordSet.remove();
            } else {
                newShouldBe[counter++] = shouldBe[i];
            }
        }

        assertFalse(recordSet.hasNext());

        compareTestContent(newShouldBe);
        checkBusyPages();
    }
}
