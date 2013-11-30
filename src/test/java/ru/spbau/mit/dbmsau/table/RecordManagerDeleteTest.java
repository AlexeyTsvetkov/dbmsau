package ru.spbau.mit.dbmsau.table;

import static org.hamcrest.CoreMatchers.*;
import org.junit.Test;

public class RecordManagerDeleteTest extends TestTableTest {
    @Test
    public void testDelete() throws Exception {
        initSQLDumpLoad("create_insert_test.sql");

        String[][] shouldBe = new String[][]{
        };

        context.getRecordManager().delete(context.getTableManager().getTable("test"));

        compareTestContent(shouldBe);
    }

}
