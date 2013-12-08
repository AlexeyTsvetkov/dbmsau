package ru.spbau.mit.dbmsau.command;

import static org.hamcrest.CoreMatchers.*;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import ru.spbau.mit.dbmsau.BaseTest;
import ru.spbau.mit.dbmsau.command.exception.CommandExecutionException;
import ru.spbau.mit.dbmsau.exception.UserError;
import ru.spbau.mit.dbmsau.table.RecordManager;
import ru.spbau.mit.dbmsau.table.exception.SemanticError;

import java.util.ArrayList;

public class InsertCommandTest extends BaseTest {

    @Test
    public void testInsert() throws Exception {
        ArrayList<String> columns = new ArrayList<>();
        ArrayList<String> values = new ArrayList<>();
        columns.add("id");values.add("1");
        columns.add("name");values.add("2");

        InsertCommand command = new InsertCommand("test", columns, values);
        command.setContext(context);

        assertNotNull(command.execute());
    }

    @Test
    public void testInsertFail() throws Exception {
        thrown.expect(UserError.class);
        thrown.expectMessage("No such table `test123`");

        ArrayList<String> columns = new ArrayList<>();
        ArrayList<String> values = new ArrayList<>();
        columns.add("id");values.add("1");
        columns.add("name");values.add("2");

        InsertCommand command = new InsertCommand("test123", columns, values);
        command.setContext(context);

        assertNotNull(command.execute());
    }

    @Test
    public void testInsertWrongType() throws Exception {
        thrown.expect(SemanticError.class);
        thrown.expectMessage("`id` should be an integer");

        ArrayList<String> columns = new ArrayList<>();
        ArrayList<String> values = new ArrayList<>();
        columns.add("id");values.add("abcde");
        columns.add("name");values.add("2");

        InsertCommand command = new InsertCommand("test", columns, values);
        command.setContext(context);

        assertNotNull(command.execute());
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
