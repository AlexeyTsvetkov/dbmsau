package ru.spbau.mit.dbmsau.table;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import ru.spbau.mit.dbmsau.BaseTest;
import ru.spbau.mit.dbmsau.Context;


import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.is;

public class FileTableManagerTest extends BaseTest {
    @Test
    public void testCreateTable() throws Exception {
        setUpContext();

        ArrayList<Column> columns = new ArrayList<>();

        columns.add(new Column("id", Type.getType(Type.TYPE_IDENTIFIER_INTEGER)));
        columns.add(new Column("varchar", Type.getType(Type.TYPE_IDENTIFIER_VARCHAR, 50)));

        Table table = new Table("test", columns);

        assertNull(context.getTableManager().getTable("test"));

        context.getTableManager().createNewTable(table);

        assertThat(context.getTableManager().getTable("test").getName(), is("test"));
    }

    @Test
    public void testLoading() throws Exception {
        FileUtils.copyFile(
            FileUtils.toFile(getClass().getResource("test.tbl")),
            tempFolder.newFile("test.tbl")
        );

        setUpContext();

        Table table = context.getTableManager().getTable("test");
        assertThat(table.getName(), is("test"));
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
