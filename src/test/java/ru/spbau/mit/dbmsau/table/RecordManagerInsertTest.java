package ru.spbau.mit.dbmsau.table;

import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;
import ru.spbau.mit.dbmsau.BaseTest;
import ru.spbau.mit.dbmsau.pages.Record;
import ru.spbau.mit.dbmsau.pages.RecordsPage;

import java.util.ArrayList;

public class RecordManagerInsertTest extends BaseTest {
    private String getNameValue(int row, RecordsPage p) {
        return new Record(p, row).getStringValue(4, 50);
    }

    private Table getTestTable() {
        return context.getTableManager().getTable("test");
    }

    private void checkOneInsertIntoTestTable(int idValue, String nameValue) throws Exception {
        ArrayList<String> columns = new ArrayList<>();
        ArrayList<String> values = new ArrayList<>();

        columns.add("id");
        values.add(Integer.valueOf(idValue).toString());
        columns.add("name");
        values.add(nameValue);

        Table table = getTestTable();

        context.getRecordManager().insert(table, columns, values);

        TableRecordsPage p = new TableRecordsPage(getTestTable(), context.getPageManager().getPageById(3, false));

        if (nameValue.length() > 50) {
            nameValue = nameValue.substring(0, 50);
        }

        assertThat(p.getByteBuffer().getInt(0), is(idValue));
        assertThat(getNameValue(0, p), is(nameValue));

        checkBusyPages();
    }

    @Test
    public void testInsert() throws Exception {
        initSQLDumpLoad("create_test.sql");

        int idValue = 123456789;
        String nameValue = "abcdefgh";

        checkOneInsertIntoTestTable(idValue, nameValue);
    }

    @Test
    public void testInsertLongString() throws Exception {
        initSQLDumpLoad("create_test.sql");

        int idValue = 123456789;
        String nameValue = "";

        for (int i = 0; i < 100; i++) {
            nameValue += (char) ((i % 26) + 'a');
        }

        checkOneInsertIntoTestTable(idValue, nameValue);
    }

    @Test
    public void testManyInserts() throws Exception {
        initSQLDumpLoad("create_test.sql");

        ArrayList<String> columns = new ArrayList<>();
        ArrayList<String> values = new ArrayList<>();

        columns.add("id");
        values.add("1");
        columns.add("name");
        values.add("2");

        Table table = context.getTableManager().getTable("test");

        TableRecordsPage p = new TableRecordsPage(table, context.getPageManager().getPageById(3, false));

        int value = 0;

        while (!p.isFull()) {
            values.set(0, Integer.valueOf(value).toString());
            values.set(1, Integer.valueOf(-value).toString());
            context.getRecordManager().insert(table, columns, values);
            value++;
        }

        for (int i = 0; i < p.getMaxRecordsCount(); i++) {
            assertThat(p.getByteBuffer().getInt(i * 54), is(i));
            assertThat(getNameValue(i, p), is(Integer.valueOf(-i).toString()));
        }

        context.getRecordManager().insert(table, columns, values);

        p = new TableRecordsPage(table, context.getPageManager().getPageById(4, false));

        int i = p.getMaxRecordsCount();
        assertThat(p.getByteBuffer().getInt(0), is(i - 1));
        assertThat(getNameValue(0, p), is(Integer.valueOf(-(i - 1)).toString()));

        checkBusyPages();
    }
}
