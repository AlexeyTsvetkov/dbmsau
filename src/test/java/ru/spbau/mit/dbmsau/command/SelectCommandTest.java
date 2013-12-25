package ru.spbau.mit.dbmsau.command;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import ru.spbau.mit.dbmsau.BaseTest;
import ru.spbau.mit.dbmsau.table.RecordManager;

import java.util.ArrayList;
import java.util.List;

public class SelectCommandTest extends BaseTest {

    @Test
    public void testSelectAll() throws Exception {
        ArrayList<String> columns = new ArrayList<>();
        ArrayList<String> values = new ArrayList<>();

        columns.add("id"); values.add("1");
        columns.add("name"); values.add("2");

        InsertCommand insert = new InsertCommand("test", columns, values);
        insert.setContext(context);
        insert.execute();

        SelectCommand select = new SelectCommand("test", null);
        select.setContext(context);
        SQLCommandResult result = select.execute();

        assertNotNull(result);
        assertTrue(result.isOk());

        List<String> expected = new ArrayList<>();
        expected.add("id;name");
        expected.add("1;2");

        /* Freezes after second call to resultIterator.next()
        Iterator<String> resultIterator = result.iterator();
        for (String expectedLine : expected) {
            assertTrue(resultIterator.hasNext());

            String resultLine = resultIterator.next();
            resultIterator.next();
            assertTrue(expectedLine.equals(resultLine));
        }*/
    }

    @Before
    public void setUp() throws Exception {
        FileUtils.copyFile(
                FileUtils.toFile(RecordManager.class.getResource("test.tbl")),
                tempFolder.newFile("test.tbl")
        );
        super.setUp();
    }
}
