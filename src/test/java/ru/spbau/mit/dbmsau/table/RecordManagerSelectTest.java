package ru.spbau.mit.dbmsau.table;

import org.junit.Test;

public class RecordManagerSelectTest extends TestTableTest {
    @Test
    public void testSelect() throws Exception {
        initSQLDumpLoad("create_insert_test.sql");

        String[][] shouldBe = new String[][]{
            {"1", "abc"}, {"3", "cde"}, {"5", "efg"}
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
        }

        insertShouldBe(shouldBe);

        compareTestContent(shouldBe);
        compareTestContent(shouldBe);
    }
}
