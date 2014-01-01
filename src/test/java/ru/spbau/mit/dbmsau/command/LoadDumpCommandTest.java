package ru.spbau.mit.dbmsau.command;

import org.junit.Test;
import ru.spbau.mit.dbmsau.table.TestTableTest;

public class LoadDumpCommandTest extends TestTableTest {
    @Test
    public void testLoadDump() throws Exception {
        initSQLDumpLoad(TestTableTest.class, "create_test.sql");

        LoadDumpCommand command = new LoadDumpCommand(
            "test",
            getResourceFileByName("test.dump").getPath()
        );

        command.setContext(context);
        command.execute();

        compareTestContent(
            new String[][]{
                {"1", "abc"}, {"3", "cde"}, {"5", "efg"}
            }
        );
    }
}
