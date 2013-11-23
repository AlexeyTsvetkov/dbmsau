package ru.spbau.mit.dbmsau.table;

import static org.hamcrest.CoreMatchers.*;
import org.junit.Test;
import ru.spbau.mit.dbmsau.BaseTest;

import java.util.ArrayList;

public class RecordManagerInsertTest extends BaseTest {
    @Test
    public void testInsert() throws Exception {
        initSQLDumpLoad("create_test.sql");

        ArrayList<String> columns = new ArrayList<>();
        ArrayList<String> values = new ArrayList<>();

        int idValue = 123456789;
        int nameValue = 987654321;

        columns.add("id");values.add(Integer.valueOf(idValue).toString());
        columns.add("name");values.add(Integer.valueOf(nameValue).toString());

        Table table = context.getTableManager().getTable("test");

        context.getRecordManager().insert(table, columns, values);

        TableRecordsPage p = new TableRecordsPage(table, context.getPageManager().getPageById(3, true));

        assertThat(p.getByteBuffer().getInt(0), is(idValue));
        assertThat(p.getByteBuffer().getInt(4), is(nameValue));

        int value = 0;

        while (!p.isFull()) {
            values.set(0, Integer.valueOf(value).toString());
            values.set(1, Integer.valueOf(-value).toString());
            context.getRecordManager().insert(table, columns, values);
            value++;
        }

        for (int i = 1; i < p.getMaxRecordsCount(); i++) {
            assertThat(p.getByteBuffer().getInt(i * 8), is(i-1));
            assertThat(p.getByteBuffer().getInt(i * 8 + 4), is(-(i-1)));
        }

        context.getRecordManager().insert(table, columns, values);

        context.getPageManager().releasePage(p);

        p = new TableRecordsPage(table, context.getPageManager().getPageById(4, false));

        int i = p.getMaxRecordsCount()-1;
        assertThat(p.getByteBuffer().getInt(0), is(i-1));
        assertThat(p.getByteBuffer().getInt(4), is(-(i-1)));

        checkBusyPages();
    }
}
