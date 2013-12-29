package ru.spbau.mit.dbmsau.relation;

import ru.spbau.mit.dbmsau.BaseTest;
import ru.spbau.mit.dbmsau.table.Table;

import java.util.ArrayList;
import java.util.Arrays;

public class TestChildrenTable {
    public static final String TABLE_NAME = "test_children";
    public static final String COLUMN_NAME_ID = "id";
    public static final String COLUMN_NAME_TEST_ID = "test_id";
    public static final String COLUMN_NAME_VALUE = "value";

    public static final int COLUMN_INDEX_ID = 0;
    public static final int COLUMN_INDEX_TEST_ID = 1;
    public static final int COLUMN_INDEX_VALUE = 2;

    private static Table childrenTable = new Table(
        TABLE_NAME,
        new ArrayList<>(
            Arrays.asList(
                new Column(TABLE_NAME, COLUMN_NAME_ID, Type.getIntegerType()),
                new Column(TABLE_NAME, COLUMN_NAME_TEST_ID, Type.getIntegerType()),
                new Column(TABLE_NAME, COLUMN_NAME_VALUE, Type.getIntegerType())
            )
        )
    );

    public static Table getTable() {
        return childrenTable;
    }

    public static String[][] testContent = new String[][]{
        new String[]{"1", "tlbv"},
        new String[]{"2", "gmtb"},
        new String[]{"3", "wyqa"},
        new String[]{"4", "bfex"},
        new String[]{"5", "myxb"},
        new String[]{"6", "xswb"},
        new String[]{"7", "kmsi"},
        new String[]{"8", "cbxv"},
        new String[]{"9", "dcna"},
        new String[]{"10", "fpqq"},
        new String[]{"11", "hqct"},
        new String[]{"12", "geol"},
        new String[]{"13", "kreb"},
        new String[]{"14", "wvgl"},
        new String[]{"15", "hrjd"},
        new String[]{"16", "pezt"},
        new String[]{"17", "zhku"},
        new String[]{"18", "ofog"},
        new String[]{"19", "mamo"},
        new String[]{"20", "vayg"},
    };

    public static String[][] testChildrenContent = new String[][]{
        new String[]{"1", "18", "500"},
        new String[]{"2", "17", "470"},
        new String[]{"3", "6", "697"},
        new String[]{"4", "16", "77"},
        new String[]{"5", "4", "710"},
        new String[]{"6", "19", "354"},
        new String[]{"7", "4", "482"},
        new String[]{"8", "11", "142"},
        new String[]{"9", "10", "807"},
        new String[]{"10", "9", "282"},
        new String[]{"11", "12", "506"},
        new String[]{"12", "6", "544"},
        new String[]{"13", "6", "924"},
        new String[]{"14", "20", "297"},
        new String[]{"15", "19", "345"},
        new String[]{"16", "20", "259"},
        new String[]{"17", "4", "781"},
        new String[]{"18", "10", "674"},
        new String[]{"19", "16", "447"},
        new String[]{"20", "17", "221"},
    };

    private static RecordSet buildRecordSetByContent(Relation relation, String[][] content) {
        StubRelationRecord[] resultContent = new StubRelationRecord[testContent.length];

        for (int i = 0; i < testContent.length; i++) {
            resultContent[i] = new StubRelationRecord(relation, content[i]);
        }

        return new ArrayRecordSet(
            relation,
            resultContent
        );
    }

    public static RecordSet buildTestRecordSet(boolean empty) {
        Relation relation = BaseTest.buildTestTable();
        if (empty) {
            return new ArrayRecordSet(relation);
        }
        return buildRecordSetByContent(relation, testContent);
    }

    public static RecordSet buildTestChildrenRecordSet(boolean empty) {
        Relation relation = getTable();
        if (empty) {
            return new ArrayRecordSet(relation);
        }
        return buildRecordSetByContent(relation, testChildrenContent);
    }

    public static String[][] getJoinResult() {
        ArrayList<String[]> result = new ArrayList<>();

        for (String[] testRow : testContent) {
            for (String[] testChildRow : testChildrenContent) {
                if (testRow[0].equals(testChildRow[COLUMN_INDEX_TEST_ID])) {
                    result.add(
                        new String[]{
                            testChildRow[COLUMN_INDEX_ID],
                            testChildRow[COLUMN_INDEX_VALUE],
                            testRow[BaseTest.TEST_COLUMN_INDEX_ID],
                            testRow[BaseTest.TEST_COLUMN_INDEX_NAME]
                        }
                    );
                }
            }
        }

        return result.toArray(new String[result.size()][]);
    }

    public static ColumnAccessor[] getResultColumnAccessors() {
        ColumnAccessor testChildrenId = new ColumnAccessor(TABLE_NAME, COLUMN_NAME_ID);
        ColumnAccessor testChildrenValue = new ColumnAccessor(TABLE_NAME, COLUMN_NAME_VALUE);
        ColumnAccessor testId = new ColumnAccessor("test", "id");
        ColumnAccessor testName = new ColumnAccessor("test", "name");

        return new ColumnAccessor[]{
            testChildrenId,
            testChildrenValue,
            testId,
            testName
        };
    }

    public static String[][] createJoinedArrayFromRecordSet(RecordSet recordSet) {
        ArrayList<String[]> result = new ArrayList<>();

        ColumnAccessor[] columnAccessors = getResultColumnAccessors();
        int[] columnIndexes = new int[columnAccessors.length];

        for (int i = 0; i < columnAccessors.length; i++) {
            columnIndexes[i] = columnAccessors[i].getColumnIndex(recordSet.getRelation());
        }

        recordSet.moveFirst();

        while (recordSet.hasNext()) {
            RelationRecord current = recordSet.next();

            String[] row = new String[columnIndexes.length];
            for (int i = 0; i < row.length; i++) {
                row[i] = current.getString(columnIndexes[i]);
            }

            result.add(row);
        }

        return result.toArray(new String[result.size()][]);
    }
}
