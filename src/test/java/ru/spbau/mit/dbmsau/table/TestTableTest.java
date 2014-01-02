package ru.spbau.mit.dbmsau.table;

import ru.spbau.mit.dbmsau.BaseTest;
import ru.spbau.mit.dbmsau.relation.MemoryRelationRecord;
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

    protected void compareRecordSetTestContent(RecordSet recordSet, String[][] shouldBe) {
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

    protected void compareTestContent(String[][] shouldBe) {
        compareRecordSetTestContent(
            context.getTableRecordManager().select(context.getTableManager().getTable("test")),
            shouldBe
        );
    }

    protected void insertShouldBe(String[][] shouldBe) throws Exception {
        Table table = context.getTableManager().getTable("test");
        MemoryRelationRecord record = new MemoryRelationRecord(table);

        for (int i = 0; i < shouldBe.length; i++) {
            record.setValueFromString(TEST_COLUMN_INDEX_ID, shouldBe[i][0]);
            record.setValueFromString(TEST_COLUMN_INDEX_NAME, shouldBe[i][1]);

            context.getTableRecordManager().insert(table, record);
        }
    }

    protected Table getTestTable() {
        return context.getTableManager().getTable("test");
    }

    public static final String[][] defaultContent = new String[][] {
        new String[]{"1", "abc"},
        new String[]{"3", "cde"},
        new String[]{"5", "efg"}
    };
}
