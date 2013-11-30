package ru.spbau.mit.dbmsau.table;

import ru.spbau.mit.dbmsau.BaseTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import static org.hamcrest.CoreMatchers.is;

public class TestTableTest extends BaseTest {
    private String[] joinArray(String[][] a) {
        String[] result = new String[a.length];

        for (int i = 0; i < a.length; i++) {
            result[i] = a[i][0] + "#" + a[i][1];
        }

        Arrays.sort(result);

        return result;
    }

    private void compareResult(String[][] shouldBe, String[][] result) {
        String[] shouldBeJoined = joinArray(shouldBe);
        String[] resultJoined = joinArray(result);
        assertArrayEquals(shouldBeJoined, resultJoined);
    }

    protected void compareTestContent(String[][] shouldBe) {

        Iterator<TableRecord> recordSet = context.getRecordManager().select(context.getTableManager().getTable("test")).iterator();

        String[][] result = new String[shouldBe.length][2];

        for (int i = 0; i < shouldBe.length; i++) {
            assertTrue(recordSet.hasNext());

            TableRecord next = recordSet.next();

            result[i][0] = next.getValueAsString("id");
            result[i][1] = next.getValueAsString("name");
        }

        assertFalse(recordSet.hasNext());

        compareResult(shouldBe, result);

        checkBusyPages();
    }
}
