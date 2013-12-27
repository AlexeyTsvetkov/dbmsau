package ru.spbau.mit.dbmsau.command;

import com.google.common.collect.Lists;
import junitx.util.PrivateAccessor;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import ru.spbau.mit.dbmsau.BaseTest;
import ru.spbau.mit.dbmsau.Context;
import ru.spbau.mit.dbmsau.relation.ColumnAccessor;
import ru.spbau.mit.dbmsau.relation.Relation;
import ru.spbau.mit.dbmsau.relation.StubRelation;
import ru.spbau.mit.dbmsau.table.StubTableManager;
import ru.spbau.mit.dbmsau.table.StubTableRecordManager;
import ru.spbau.mit.dbmsau.table.TableManager;
import ru.spbau.mit.dbmsau.table.TableRecordManager;
import ru.spbau.mit.dbmsau.table.exception.SemanticError;

import java.util.ArrayList;
import java.util.Arrays;

public class SelectCommandTest extends BaseTest {

    @Test
    public void testSelectAll() throws Exception {
        ArrayList<String> columns = new ArrayList<>();
        ArrayList<String> values = new ArrayList<>();

        columns.add("id");
        values.add("1");
        columns.add("name");
        values.add("2");

        InsertCommand insert = new InsertCommand("test", columns, values);
        insert.setContext(context);
        insert.execute();

        /*SelectCommand select = new SelectCommand("test", null);
        select.setContext(context);
        SQLCommandResult result = select.execute();

        assertNotNull(result);
        assertTrue(result.isOk());

        List<String> expected = new ArrayList<>();
        expected.add("id;name");
        expected.add("1;2");

         Freezes after second call to resultIterator.next()
        Iterator<String> resultIterator = result.moveFirst();
        for (String expectedLine : expected) {
            assertTrue(resultIterator.hasNext());

            String resultLine = resultIterator.next();
            resultIterator.next();
            assertTrue(expectedLine.equals(resultLine));
        }*/
    }

    private Relation buildStubRelation() {
        return new StubRelation("test", "id", "name", "abc");
    }

    private int[] invokeBuildColumnIndexesToSelect(SelectCommand command) throws Throwable {
        return (int[]) PrivateAccessor.invoke(
            command, "buildColumnsIndexesToSelect",
            new Class[]{Relation.class},
            new Object[]{
                buildStubRelation()
            }
        );
    }

    @Test
    public void testBuildColumnsIndexesToSelect() throws Throwable {
        SelectCommand command = new SelectCommand(
            Arrays.asList(
                new ColumnAccessor(null, "name"),
                new ColumnAccessor("test", "id"),
                new ColumnAccessor(null, "abc")
            ),
            "test",
            null
        );
        command.setContext(context);

        int[] result = invokeBuildColumnIndexesToSelect(command);
        assertArrayEquals(result, new int[]{1, 0, 2});

        command = new SelectCommand(
            null,
            "test",
            null
        );
        command.setContext(context);

        result = invokeBuildColumnIndexesToSelect(command);
        assertArrayEquals(result, new int[]{0, 1, 2});
    }

    @Test
    public void testBuildColumnsIndexesToSelectError() throws Throwable {
        thrown.expect(SemanticError.class);
        thrown.expectMessage("Unknown column `abcee`");

        SelectCommand command = new SelectCommand(
            Arrays.asList(
                new ColumnAccessor(null, "name"),
                new ColumnAccessor("test", "id"),
                new ColumnAccessor(null, "abcee")
            ),
            "test",
            null
        );
        command.setContext(context);

        invokeBuildColumnIndexesToSelect(command);
    }

    @Test
    public void testSelectWithColumns() throws Exception {
        SelectCommand command = new SelectCommand(
            Arrays.asList(
                new ColumnAccessor(null, "name"),
                new ColumnAccessor("test", "id")
            ),
            "test",
            null
        );
        command.setContext(context);

        SQLCommandResult result = command.execute();

        assertTrue(result.isIterable());

        Object[] queryOutput = Lists.newArrayList(result.iterator()).toArray();

        assertArrayEquals(
            new String[]{
                "test.name:varchar(50);test.id:integer",
                "asd;1",
                "def;2"
            },
            queryOutput
        );
    }

    @Test
    public void testSelectWithAsterisk() throws Exception {
        SelectCommand command = new SelectCommand(
            null,
            "test",
            null
        );
        command.setContext(context);

        SQLCommandResult result = command.execute();

        assertTrue(result.isIterable());

        Object[] queryOutput = Lists.newArrayList(result.iterator()).toArray();

        assertArrayEquals(
            new String[]{
                "test.id:integer;test.name:varchar(50)",
                "1;asd",
                "2;def"
            },
            queryOutput
        );
    }

    @Override
    protected TableManager buildTableManager(Context context) {
        TableManager tableManager = new StubTableManager(context);
        try {
            tableManager.createNewTable(buildTestTable());
        } catch (Exception e) {

        }
        return tableManager;
    }

    @Override
    protected TableRecordManager buildTableRecordManager(Context context) {
        return new StubTableRecordManager(context);
    }

    @Before
    public void setUp() throws Exception {
        FileUtils.copyFile(
            FileUtils.toFile(TableRecordManager.class.getResource("test.tbl")),
            tempFolder.newFile("test.tbl")
        );
        super.setUp();
    }
}
