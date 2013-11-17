package ru.spbau.mit.dbmsau.table;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import ru.spbau.mit.dbmsau.BaseTest;
import ru.spbau.mit.dbmsau.Context;
import ru.spbau.mit.dbmsau.table.exception.TableManagerException;


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
    public void testAlreadyExists() throws Exception {
        thrown.expect(TableManagerException.class);
        thrown.expectMessage("Table `" + TEST_TABLE_NAME + "` already exists");

        setUpContext();
        createTestTable();
        createTestTable();
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

        ArrayList< Column > columns = table.getColumns();

        assertThat(columns.size(), is(2));

        assertThat(columns.get(0).toString(), is("id:integer"));
        assertThat(columns.get(1).toString(), is("name:varchar(50)"));
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
