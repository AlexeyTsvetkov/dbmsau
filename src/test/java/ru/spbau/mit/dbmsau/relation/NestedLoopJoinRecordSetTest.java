package ru.spbau.mit.dbmsau.relation;

import org.junit.Test;
import ru.spbau.mit.dbmsau.BaseTest;

import java.util.Arrays;
import java.util.Comparator;

public class NestedLoopJoinRecordSetTest extends BaseTest {
    private Comparator<String[]> recordComparator = new Comparator<String[]>() {
        @Override
        public int compare(String[] o1, String[] o2) {
            for (int i = 0; i < o1.length; i++) {
                if (!o1[i].equals(o2[i])) {
                    return o1[i].compareTo(o2[i]);
                }
            }

            return 0;
        }
    };

    private void checkJoinedRecordSet(RecordSet recordSet) {
        String[][] was = TestChildrenTable.createJoinedArrayFromRecordSet(recordSet);
        String[][] expected = TestChildrenTable.getJoinResult();

        assertTrue(was.length == expected.length);
        Arrays.sort(was, recordComparator);
        Arrays.sort(expected, recordComparator);

        for (int i = 0; i < expected.length; i++) {
            assertArrayEquals(expected[i], was[i]);
        }
    }

    @Test
    public void testJoin() throws Exception {
        //children JOIN test
        JoinedRelation joinedRelation = new JoinedRelation(
            TestChildrenTable.getTable(),
            buildTestTable(),
            TestChildrenTable.COLUMN_INDEX_TEST_ID,
            TEST_COLUMN_INDEX_ID
        );

        NestedLoopJoinRecordSet recordSet = new NestedLoopJoinRecordSet(
            joinedRelation,
            TestChildrenTable.buildTestChildrenRecordSet(false),
            TestChildrenTable.buildTestRecordSet(false)
        );

        checkJoinedRecordSet(recordSet);

        //test JOIN children
        joinedRelation = new JoinedRelation(
            buildTestTable(),
            TestChildrenTable.getTable(),
            TEST_COLUMN_INDEX_ID,
            TestChildrenTable.COLUMN_INDEX_TEST_ID
        );

        recordSet = new NestedLoopJoinRecordSet(
            joinedRelation,
            TestChildrenTable.buildTestRecordSet(false),
            TestChildrenTable.buildTestChildrenRecordSet(false)
        );

        checkJoinedRecordSet(recordSet);
    }

    @Test
    public void testEmptyJoin() throws Exception {
        JoinedRelation joinedRelation = new JoinedRelation(
            TestChildrenTable.getTable(),
            buildTestTable(),
            TestChildrenTable.COLUMN_INDEX_TEST_ID,
            TEST_COLUMN_INDEX_ID
        );

        NestedLoopJoinRecordSet recordSet = new NestedLoopJoinRecordSet(
            joinedRelation,
            TestChildrenTable.buildTestChildrenRecordSet(true),
            TestChildrenTable.buildTestRecordSet(false)
        );
        recordSet.moveFirst();
        assertFalse(recordSet.hasNext());

        joinedRelation = new JoinedRelation(
            TestChildrenTable.getTable(),
            buildTestTable(),
            TestChildrenTable.COLUMN_INDEX_TEST_ID,
            TEST_COLUMN_INDEX_ID
        );

        recordSet = new NestedLoopJoinRecordSet(
            joinedRelation,
            TestChildrenTable.buildTestChildrenRecordSet(false),
            TestChildrenTable.buildTestRecordSet(true)
        );
        recordSet.moveFirst();
        assertFalse(recordSet.hasNext());

    }
}
