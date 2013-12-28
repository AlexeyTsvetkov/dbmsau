package ru.spbau.mit.dbmsau.table;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import ru.spbau.mit.dbmsau.BaseTest;
import ru.spbau.mit.dbmsau.Context;
import ru.spbau.mit.dbmsau.relation.Column;
import ru.spbau.mit.dbmsau.relation.Type;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.is;

public class FileTableManagerTest extends BaseTest {
    private static final String TEST_TABLE_NAME = "test";

    private void createTestTable() throws Exception {
        ArrayList<Column> columns = new ArrayList<>();

        columns.add(new Column("id", Type.getType(Type.TYPE_IDENTIFIER_INTEGER)));
        columns.add(new Column("varchar", Type.getType(Type.TYPE_IDENTIFIER_VARCHAR, 50)));

        Table table = new Table(TEST_TABLE_NAME, columns);

        context.getTableManager().createNewTable(table);
        checkBusyPages();
    }

    @Test
    public void testCreateTable() throws Exception {
        setUpContext();

        assertNull(context.getTableManager().getTable(TEST_TABLE_NAME));

        createTestTable();

        assertThat(context.getTableManager().getTable(TEST_TABLE_NAME).getName(), is(TEST_TABLE_NAME));

        Table table = context.getTableManager().getTable(TEST_TABLE_NAME);
        assertThat(table.getFullPagesListHeadPageId(), is(1));
        assertThat(table.getNotFullPagesListHeadPageId(), is(2));
    }

    @Test
    public void testLoading() throws Exception {
        FileUtils.copyFile(
            FileUtils.toFile(getClass().getResource("test.tbl")),
            tempFolder.newFile("test.tbl")
        );

        setUpContext();

        Table table = context.getTableManager().getTable(TEST_TABLE_NAME);
        assertThat(table.getName(), is(TEST_TABLE_NAME));
        assertThat(table.getFullPagesListHeadPageId(), is(1));
        assertThat(table.getNotFullPagesListHeadPageId(), is(2));

        assertThat(table.getColumnsCount(), is(2));

        assertThat(table.getColumnDescription(TEST_COLUMN_INDEX_ID), is("test.id:integer"));
        assertThat(table.getColumnDescription(TEST_COLUMN_INDEX_NAME), is("test.name:varchar(50)"));

        assertThat(table.getColumnIndex("id"), is(TEST_COLUMN_INDEX_ID));
        assertThat(table.getColumnIndex("name"), is(TEST_COLUMN_INDEX_NAME));

        assertThat(table.getColumnIndex("test", "id"), is(TEST_COLUMN_INDEX_ID));
        assertThat(table.getColumnIndex("test", "name"), is(TEST_COLUMN_INDEX_NAME));
    }

    @Override
    @Before
    public void setUp() throws Exception {

    }

    @Override
    protected TableManager buildTableManager(Context context) {
        return new FileTableManager(context);
    }
}
