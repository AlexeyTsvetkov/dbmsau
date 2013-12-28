package ru.spbau.mit.dbmsau.table;

import ru.spbau.mit.dbmsau.BaseTest;
import ru.spbau.mit.dbmsau.relation.RecordSet;
import ru.spbau.mit.dbmsau.relation.RelationRecord;

import java.util.Arrays;

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

        RecordSet recordSet = context.getTableRecordManager().select(context.getTableManager().getTable("test"));
        recordSet.moveFirst();

        String[][] result = new String[shouldBe.length][2];

        for (int i = 0; i < shouldBe.length; i++) {
            assertTrue(recordSet.hasNext());

            RelationRecord next = recordSet.next();

            result[i][0] = next.getValueAsString(TEST_COLUMN_INDEX_ID);
            result[i][1] = next.getValueAsString(TEST_COLUMN_INDEX_NAME);
        }

        assertFalse(recordSet.hasNext());

        compareResult(shouldBe, result);

        checkBusyPages();
    }
}
